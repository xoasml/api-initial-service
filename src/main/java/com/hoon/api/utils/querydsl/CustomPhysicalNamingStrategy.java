package com.hoon.api.utils.querydsl;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class CustomPhysicalNamingStrategy implements PhysicalNamingStrategy {

    /**
     * 명시된 이름이 있는 경우 명시 대로 사용합니다.
     * 그렇지 않은 경우 카멜케이스를 스네이크 케이스로 변환합니다.
     */

    // 카탈로그 이름 변환 로직
    @Override
    public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment context) {
        return convertIfNeeded(name);
    }

    // 스키마 이름 변환 로직
    @Override
    public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment context) {
        return convertIfNeeded(name);
    }

    // 테이블 이름 변환 로직
    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        return convertIfNeeded(name);
    }

    // 시퀀스 이름 변환 로직
    @Override
    public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment context) {
        return convertIfNeeded(name);
    }

    //컬럼 이름 변환 로직
    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
        return convertIfNeeded(name);
    }

    private Identifier convertIfNeeded(Identifier identifier) {
        // 명시된 이름이 있는 경우 명시 대로 사용합니다.
        if (identifier != null && identifier.isQuoted()) {
            return identifier;
        }
        // 그렇지 않은 경우 카멜케이스를 스네이크 케이스로 변환합니다.
        return convertToSnakeCase(identifier);
    }

    private Identifier convertToSnakeCase(Identifier identifier) {
        if (identifier == null || identifier.getText().isEmpty()) {
            return identifier;
        }
        String newName = identifier.getText().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
        return Identifier.toIdentifier(newName);
    }

}
