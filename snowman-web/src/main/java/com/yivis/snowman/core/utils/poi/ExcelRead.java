package com.yivis.snowman.core.utils.poi;

import com.yivis.snowman.core.utils.base.ReflectUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.management.AttributeList;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
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
public abstract class ExcelRead {

    public static final String OFFICE_EXCEL_2003_POSTFIX = "xls";
    public static final String OFFICE_EXCEL_2010_POSTFIX = "xlsx";

    public static final String EMPTY = "";
    public static final String POINT = ".";

    public static final String OBJECT = "object";
    public static final String STRING = "string";

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 解析Excel
     *
     * @param entity,rule,filePath 存储的实体类，定义存储的规则，文件路径
     * @Author XuGuang  2017/3/15 01:27
     */
    public List<Object> parserExcel(Object entity, String[] rule, String filePath) throws IOException {
        List<Object> parseSheet = new ArrayList<Object>();
        File file = new File(filePath);
        FileInputStream input = new FileInputStream(file);
        String postfix = getPostfix(file.getName());
        if (file == null || EMPTY.equals(file.getName().trim())) {
            return null;
        } else {
            Workbook workbook = createWorkbook(input, postfix);
            parseSheet = pareseSheet(entity, rule, workbook);
        }
        return parseSheet;
    }

    /**
     * 解析Excel
     *
     * @param entity,rule,file 存储的实体类，定义存储的规则，file文件流
     * @Author XuGuang  2017/3/15 01:27
     */
    public List<Object> parserExcel(Object entity, String[] rule, MultipartFile file) {
        List<Object> parseSheet = new ArrayList<Object>();
        if (file == null || EMPTY.equals(file.getOriginalFilename().trim())) {
            return null;
        } else {
            String postfix = getPostfix(file.getOriginalFilename());
            Workbook workbook = createWorkbook(getInput(file), postfix);
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
            String postfix = getPostfix(file.getOriginalFilename());
            Workbook workbook = createWorkbook(getInput(file), postfix);
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
    public Object parserBean(Object entity, String[] rule, List<Object> rowData) {
        int num = 0;
        Object obj = new Object();
        try {
            obj = newInstance(entity.getClass().getName().toString(), new Object[0]);
            Iterator<Object> iterator = rowData.iterator();
            while (iterator.hasNext()) {
                String name = rule[num];
                Field field = entity.getClass().getDeclaredField(name);
                String fieldType = field.getType().getSimpleName().toString();
                Object args = iterator.next();
                if ("Integer".equals(fieldType) || "int".equals(fieldType)) {
                    args = Integer.parseInt(args.toString());
                } else if ("Long".equalsIgnoreCase(fieldType)) {
                    args = Long.parseLong(args.toString());
                } else if ("Double".equalsIgnoreCase(fieldType)) {
                    args = Double.parseDouble(args.toString());
                } else if ("Boolean".equalsIgnoreCase(fieldType)) {
                    args = Boolean.parseBoolean(args.toString());
                }
                ReflectUtils.invokeSetter(obj, name, args);
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
     * 获取文件流
     */
    public InputStream getInput(MultipartFile file) {
        InputStream input = null;
        try {
            input = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return input;
    }

    /**
     * 创建WorkBook
     */
    public Workbook createWorkbook(InputStream input, String postfix) {
        try {
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
        }
        return null;
    }

    /**
     * 解析表格
     */
    public List<Object> pareseSheet(Object entity, String[] rule, Workbook workbook) {
        List<Object> list = new ArrayList<Object>();
        Sheet sheet = null;
        for (int sheetCount = 0; sheetCount < workbook.getNumberOfSheets(); sheetCount++) {
            sheet = workbook.getSheetAt(sheetCount);
            Iterator<Row> rowIterator = sheet.iterator();
            int rowNumber = 0;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                List<Object> rowData = parseRow(row, OBJECT);
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
                List<Object> rowData = parseRow(row, STRING);
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
    public List<Object> parseRow(Row row, String backType) {
        int columnNumber = 0;
        List<Object> rowData = new ArrayList<Object>();
        // 迭代 一行的各个单元格
        Iterator<Cell> cellIterator = row.iterator();
        // 遍历一行多列
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            Object cellObj = null;
            if (OBJECT.equals(backType)) {  //返回object
                cellObj = parseCell(cell, columnNumber);
            } else if (STRING.equals(backType)) {   //返回String
                cellObj = parseCell(cell);
            }
            rowData.add(cellObj);
            columnNumber++;
        }
        return rowData;
    }

    /**
     * 自定义处理指定列单元格 子类重写
     *
     * @param
     * @Author XuGuang  2017/3/15 10:13
     */
    public Object parserSpecifySheet(Cell cell, int columnNumber) {
        double value = cell.getNumericCellValue();
        CellStyle style = cell.getCellStyle();
        DecimalFormat format = new DecimalFormat();
        String temp = style.getDataFormatString();
        // 单元格设置成常规
//        if (temp.equals("General")) {
//            format.applyPattern("#.##");
//        }
        return format.format(value);
    }

    /**
     * 解析单元格 返回原数据格式
     *
     * @param cell
     * @return
     */
    protected Object parseCell(Cell cell, int columnNumber) {
        Object obj = null;
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                obj = cell.getBooleanCellValue();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                // 处理日期格式、时间格式
                if (DateUtil.isCellDateFormatted(cell)) {
                    obj = cell.getDateCellValue();
                } else if (cell.getCellStyle().getDataFormat() == 58) {
                    // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                    obj = DateUtil.getJavaDate(cell.getNumericCellValue());
                } else {
                    obj = parserSpecifySheet(cell, columnNumber);   //自定义处理数据格式
                }
                break;
            default:
                obj = cell.getStringCellValue();
                break;
        }
        return obj;
    }

    /**
     * 解析单元格格式 返回String字符串
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
