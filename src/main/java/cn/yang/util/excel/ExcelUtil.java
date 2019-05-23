package cn.yang.util.excel;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneId;
import java.util.*;

/**
 * @author yang 2019/5/18
 */
public class ExcelUtil implements ExcelHelper {

    private Workbook workbook;

    ExcelUtil(InputStream inputStream) throws IOException {
        try {
            this.workbook = new XSSFWorkbook(inputStream);
        } catch (IOException e) {
            this.workbook = new HSSFWorkbook(inputStream);
        }
    }

    ExcelUtil(String typeString) {
        try {
            Type type = Type.valueOf(typeString);
            if (type == Type.XLS) {
                this.workbook = new HSSFWorkbook();
            } else if (type == Type.XLSX) {
                this.workbook = new XSSFWorkbook();
            } else {
                throw new RuntimeException("Cannot create workbook");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid excel file type: -->" + typeString);
        }
    }

    @Override
    public List<Map<String, Object>> read(int sheetIndex, String... titleNames) {
        List<Map<String, Object>> result = new LinkedList<>();
        Sheet sheet = this.workbook.getSheetAt(sheetIndex);

        Map<String, Object> rowResult;

        int firstRowNum = sheet.getFirstRowNum();
        Map<Integer, String> titleNameConfig = nameConfig(sheet.getRow(firstRowNum), titleNames);

        Iterator<Row> rowIterator = sheet.iterator();
        // 跳过表头行
        for (int i = 0; i <= firstRowNum && rowIterator.hasNext(); i++) {
            rowIterator.next();
        }
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            rowResult = new LinkedHashMap<>();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                int columnIndex = cell.getColumnIndex();
                if (columnIndex >= titleNameConfig.size()) {
                    break;
                }
                String columnName = titleNameConfig.get(columnIndex);
                rowResult.put(columnName, getCellValue(cell));
            }
            result.add(rowResult);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> read(int sheetIndex, Map<Integer, String> titleName) {
        List<String> list = new ArrayList<>(titleName.size());
        titleName.keySet().stream().sorted()
                .forEach(i -> list.add(titleName.get(i)));
        String[] names = list.toArray(new String[0]);
        return read(sheetIndex, names);
    }

    /**
     * 获取表头列名
     *
     * @param title      工作表表头行
     * @param titleNames 列名
     * @return 表头Map配置
     */
    private Map<Integer, String> nameConfig(Row title, String... titleNames) {
        Map<Integer, String> titleNameConfig = new HashMap<>(titleNames.length);
        Iterator<Cell> titleCells = title.cellIterator();
        if (titleNames.length > 0) {
            for (int index = 0, length = titleNames.length; index < length && titleCells.hasNext(); index++) {
                titleCells.next();
                titleNameConfig.put(index, titleNames[index]);
            }
        } else {
            for (int index = 0; titleCells.hasNext(); index++) {
                Cell next = titleCells.next();
                titleNameConfig.put(index, next.toString());
            }
        }

        return titleNameConfig;
    }

    private Object getCellValue(Cell cell) {
        switch (cell.getCellTypeEnum()) {
            case _NONE:
                return "".trim();
            case BLANK:
                return "".trim();
            case STRING:
                return cell.getStringCellValue().trim();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case NUMERIC: {
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                } else {
                    return cell.getNumericCellValue();
                }
            }
            default:
                return "".trim();
        }
    }

}
