package com.yivis.snowman.core.utils.poi;

import com.yivis.snowman.core.utils.base.ReflectUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.management.AttributeList;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by XuGuang on 2017/3/14.
 * 读取Excel
 */
public abstract class ExcelRead<E> {

    public static int totalRows; //sheet中总行数
    public static int totalCells; //每一行总单元格数

    public static final String OFFICE_EXCEL_2003_POSTFIX = "xls";
    public static final String OFFICE_EXCEL_2010_POSTFIX = "xlsx";

    public static final String EMPTY = "";
    public static final String POINT = ".";

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 解析Excel
     */
    public List<Object> parserExcel(E entity, String[] rule, MultipartFile file) {
        List<Object> parseSheet = new ArrayList<Object>();
        if (file == null || EMPTY.equals(file.getOriginalFilename().trim())) {
            return null;
        } else {
            Workbook workbook = createWorkbook(file);
            parseSheet = pareseSheet(entity, rule, workbook);
        }
        return parseSheet;
    }

    /**
     * 解析excel
     *
     * @param file
     * @return List
     * @Author XuGuang  2017/3/14 23:47
     */
    public List<Object> parserExcel(MultipartFile file) {
        List<Object> parseSheet = new ArrayList<Object>();
        if (file == null || EMPTY.equals(file.getOriginalFilename().trim())) {
            return null;
        } else {
            Workbook workbook = createWorkbook(file);
            parseSheet = pareseSheet(workbook);
        }
        return parseSheet;
    }

    /**
     * 得到带构造的类的实例
     */
    public Object newInstance(String className, Object[] args) throws Exception {
        Class newoneClass = Class.forName(className);
        Class[] argsClass = new Class[args.length];
        for (int i = 0, j = args.length; i < j; i++) {
            argsClass[i] = args[i].getClass();
        }
        Constructor cons = newoneClass.getConstructor(argsClass);
        return cons.newInstance(args);
    }

    /**
     * 单元格行数据转换为bean
     */
    public Object parserBean(E entity, String[] rule, List<Object> rowData) {
        int num = 0;
        Object obj = new Object();
        try {
            obj = newInstance(entity.getClass().getName().toString(), new Object[0]);
            Iterator<Object> iterator = rowData.iterator();
            while (iterator.hasNext()) {
                Object args = iterator.next();
                String name = rule[num];

                ReflectUtils.invokeSetter(obj, name, args);


//                Field field =  entity.getClass().getDeclaredField(name);
//                String type = field.getType().toString();
//                name = name.substring(0, 1).toUpperCase() + name.substring(1); // 将属性的首字符大写，方便构造get，set方法
//                if (type.equals("class java.lang.String")) {
//                    Method method = entity.getClass().getMethod("set" + name);
//                    method.invoke(entity, args);
//                }
                num++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 获得path的后缀名
     *
     * @param path
     * @return
     */
    public String getPostfix(String path) {
        if (path == null || EMPTY.equals(path.trim())) {
            return EMPTY;
        }
        if (path.contains(POINT)) {
            return path.substring(path.lastIndexOf(POINT) + 1, path.length());
        }
        return EMPTY;
    }

    /**
     * 创建WorkBook
     */
    public Workbook createWorkbook(MultipartFile file) {
        // IO流读取文件
        InputStream input = null;
        String postfix = getPostfix(file.getOriginalFilename());
        try {
            input = file.getInputStream();
            if (!EMPTY.equals(postfix)) {
                if (OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {  //判断是否为2003格式的excel
                    return new HSSFWorkbook(input);
                } else if (OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {   //判断是否为2010格式的excel
                    return new XSSFWorkbook(input);
                } else {
                    return null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 解析表格
     */
    public List<Object> pareseSheet(E entity, String[] rule, Workbook workbook) {
        List<Object> list = new ArrayList<Object>();
        Sheet sheet = null;
        for (int sheetCount = 0; sheetCount < workbook.getNumberOfSheets(); sheetCount++) {
            sheet = workbook.getSheetAt(sheetCount);
            Iterator<Row> rowIterator = sheet.iterator();
            int rowNumber = 0;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                List<Object> rowData = parseRow(row);
                if (rowNumber > 0) {
                    list.add(parserBean(entity, rule, rowData));
                }
                rowNumber++;
            }
        }
        return list;
    }

    /**
     * 解析表格
     */
    public List<Object> pareseSheet(Workbook workbook) {
        List<Object> listSheet = new AttributeList();
        Sheet sheet = null;
        for (int sheetCount = 0; sheetCount < workbook.getNumberOfSheets(); sheetCount++) {     //循环遍历每个sheet
            List<List<Object>> list = new ArrayList<List<Object>>();
            sheet = workbook.getSheetAt(sheetCount);
            Iterator<Row> rowIterator = sheet.iterator();
            int rowNumber = 0;
            while (rowIterator.hasNext()) {     //迭代遍历每一行
                Row row = rowIterator.next();
                List<Object> rowData = parseRow(row);
                if (rowNumber > 0) {    //过滤表头数据
                    list.add(rowData);
                }
                rowNumber++;
            }
            listSheet.add(list);
        }
        return listSheet;
    }

    /**
     * 解析行
     *
     * @param row
     * @return
     */
    public List<Object> parseRow(Row row) {
        List<Object> rowData = new ArrayList<Object>();
        // for循环遍历
        /*for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            Object cellObj=null;
            if(cell!=null){
                cellObj = parseCell(cell);
            }
            rowData.add(cellObj);
        }*/

        // 迭代 一行的各个单元格
        Iterator<Cell> cellIterator = row.iterator();
        // 遍历一行多列
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            Object cellObj = parseCell(cell);
            rowData.add(cellObj);
        }
        return rowData;
    }

    /**
     * 解析单元格格式
     */
    public String parseCell(Cell cell) {
        String cellValue = null;
        int cellType = cell.getCellType();
        switch (cellType) {
            case Cell.CELL_TYPE_BOOLEAN:    // Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_NUMERIC:    // 数字
                if (DateUtil.isCellDateFormatted(cell) || cell.getCellStyle().getDataFormat() == 58) { // 处理日期格式、时间格式
                    // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                    Date date = DateUtil.getJavaDate(cell.getNumericCellValue());
                    cellValue = sdf.format(date);
                } else {
                    DecimalFormat df = new DecimalFormat("#.##");
                    cellValue = df.format(cell.getNumericCellValue());
                    String strArr = cellValue.substring(cellValue.lastIndexOf(POINT) + 1, cellValue.length());
                    if (strArr.equals("00")) {
                        cellValue = cellValue.substring(0, cellValue.lastIndexOf(POINT));
                    }
                }
                break;
            default:
                cellValue = cell.getStringCellValue();
                break;
        }
        return cellValue;
    }
}
