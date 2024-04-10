package com.hoon.api.utils.advice;

import com.hoon.api.utils.advice.dto.DBError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Slf4j
@Component
public class AdviceUtils {

    /**
     * Exception 내용 프린트
     */
    public static void exceptionInfoPrint(Exception e) {
        StackTraceElement[] ste = e.getStackTrace();
        StringBuffer str = new StringBuffer();
        int lastIndex = ste.length - 1;
        int count = 1;
        for (int i = lastIndex; i > lastIndex - 3; i--) {
            String className = ste[i].getClassName();
            String methodName = ste[i].getMethodName();
            int lineNumber = ste[i].getLineNumber();
            String fileName = ste[i].getFileName();

            str.append("\n").append("[" + count++ + "]").append("className :").append(className).append("\n").append("methodName :").append(methodName).append("\n").append("fileName :").append(fileName).append("\n").append("lineNumber :").append(lineNumber).append("\n").append("message :").append(e.getMessage()).append("\n").append("cause :").append(e.getCause()).append("\n");
        }
        log.error(str.toString());
    }

    /**
     * DBError 메세지 세팅
     */
    public static DBError dbErrorSetMassage(SQLException sqlException){

        DBError dbError = new DBError();

        String message = sqlException.getMessage();
        String sqlState = sqlException.getSQLState();
        String errorCode = String.valueOf(sqlException.getErrorCode());

        if ("23000".equals(sqlState) && "1062".equals(errorCode)) {
            message = "Key 값이 중복되어 Unique 위배 됩니다. > " + message;
        } else if ("23000".equals(sqlState) && "1451".equals(errorCode)) {
            message = "Key 값에 부모 레코드가 존재 하여 FK에 위배 됩니다. > " + message;
        } else if ("23000".equals(sqlState) && "1452".equals(errorCode)) {
            message = "Key 값에 부모 레코드가 존재 하지 않아 추가 또는 수정할 수 없습니다. > " + message;
        } else if ("22001".equals(sqlState) && "1406".equals(errorCode)) {
            message = "컬럼에 할당 가능한 최대 길이보다 긴 데이터를 삽입할 수 없습니다. > " + message;
        }

        dbError.setMessage(message);
        dbError.setSqlState(sqlState);
        dbError.setErrorCode(errorCode);

        return dbError;

        /*

            Duplicate entry: 중복된 값으로 인해 Unique Constraint를 위반한 경우입니다.
            SQLState: 23000
            ErrorCode: 1062

            Cannot delete or update a parent row: 부모 레코드가 있는 경우 자식 레코드를 삭제하거나 업데이트할 수 없는 경우입니다.
            SQLState: 23000
            ErrorCode: 1451

            Data too long for column: 컬럼에 할당 가능한 최대 길이보다 긴 데이터를 삽입하려는 경우입니다.
            SQLState: 22001
            ErrorCode: 1406

            Column count doesn't match value count: INSERT나 UPDATE 시 컬럼 수와 값 수가 일치하지 않는 경우입니다.
            SQLState: 21S01
            ErrorCode: 1136

            Table doesn't exist: 존재하지 않는 테이블을 참조하려는 경우입니다.
            SQLState: 42S02
            ErrorCode: 1146

            Cannot add or update a child row: 부모 키 값이 존재하지 않는 경우 자식 레코드를 추가하거나 업데이트할 수 없는 경우입니다.
            SQLState: 23000
            ErrorCode: 1452

            Lock wait timeout exceeded: 트랜잭션에서 락 대기 시간이 초과한 경우입니다.
            SQLState: 40001
            ErrorCode: 1205

            Deadlock found when trying to get lock: 데드락이 발생한 경우입니다.
            SQLState: 40001
            ErrorCode: 1213

            Unknown database: 존재하지 않는 데이터베이스를 선택하려는 경우입니다.
            SQLState: 42000
            ErrorCode: 1049

         */
    }

}
