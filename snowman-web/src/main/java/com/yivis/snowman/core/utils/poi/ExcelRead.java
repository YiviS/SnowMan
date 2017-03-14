package com.yivis.snowman.core.utils.poi;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
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

    public static final String EMPTY = "";
    public static final String POINT = ".";

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 解析Excel
     */
    public List<E> parserExcel(E entity, String[] rule, MultipartFile file) {
        List<E> list = new ArrayList<E>();
        List<List<Object>> parseSheet = new ArrayList<List<Object>>();
        if (file == null || ExcelUtil.EMPTY.equals(file.getOriginalFilename().trim())) {
            return null;
        } else {
            Workbook workbook = createWorkbook(file);
            parseSheet = pareseSheet(entity, rule, workbook);
        }
        return list;
    }

    /**
     * 单元格行数据转换为bean
     */
    public E parserBean(E entity, String[] rule, List<Object> rowData) {
        int num = 0;
        try {
            Iterator<Object> iterator = rowData.iterator();
            while (iterator.hasNext()) {
                Object obj = iterator.next();
                String name = rule[num];
                String type = entity.getClass().getField(name).getType().toString();
                name = name.substring(0, 1).toUpperCase() + name.substring(1); // 将属性的首字符大写，方便构造get，set方法
                if (type.equals("class java.lang.String")) {
                    Method method = entity.getClass().getMethod("set" + name);
                    method.invoke(entity, obj);
                }
                num++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    /**
     * 解析excel
     */
    public List<List<Object>> parserExcel(MultipartFile file) {
        List<List<Object>> parseSheet = new ArrayList<List<Object>>();
        if (file == null || ExcelUtil.EMPTY.equals(file.getOriginalFilename().trim())) {
            return null;
        } else {
            Workbook workbook = createWorkbook(file);
            parseSheet = pareseSheet(workbook);
        }
        return parseSheet;
    }

    /**
     * 创建WorkBook
     */
    public Workbook createWorkbook(MultipartFile file) {
        // IO流读取文件
        InputStream input = null;
        String postfix = ExcelUtil.getPostfix(file.getOriginalFilename());
        try {
            input = file.getInputStream();
            if (!ExcelUtil.EMPTY.equals(postfix)) {
                if (ExcelUtil.OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {  //判断是否为2003格式的excel
                    return new HSSFWorkbook(input);
                } else if (ExcelUtil.OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {   //判断是否为2010格式的excel
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
    public List<List<Object>> pareseSheet(E entity, String[] rule, Workbook workbook) {
        List<List<Object>> list = new ArrayList<List<Object>>();
        Sheet sheet = null;
        for (int sheetCount = 0; sheetCount < workbook.getNumberOfSheets(); sheetCount++) {
            sheet = workbook.getSheetAt(sheetCount);
            Iterator<Row> rowIterator = sheet.iterator();
            int rowNumber = 0;
            while (rowIterator.hasNext()) {
                if (rowNumber == 0) {
                    continue;
                } else {
                    Row row = rowIterator.next();
                    List<Object> rowData = parseRow(row);
                    parserBean(entity, rule, rowData);
                    list.add(rowData);
                }
                rowNumber++;
            }
        }
        return list;
    }

    /**
     * 解析表格
     */
    public List<List<Object>> pareseSheet(Workbook workbook) {
        List<List<Object>> list = new ArrayList<List<Object>>();
        Sheet sheet = null;
        for (int sheetCount = 0; sheetCount < workbook.getNumberOfSheets(); sheetCount++) {     //循环遍历每个sheet
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
        }
        return list;
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
