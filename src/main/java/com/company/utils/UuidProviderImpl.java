package com.company.utils;

import java.util.UUID;

public class UuidProviderImpl implements UuidProvider {
    @Override
    public String GenerateUUID() {
        return UUID.randomUUID().toString();
    }
}
