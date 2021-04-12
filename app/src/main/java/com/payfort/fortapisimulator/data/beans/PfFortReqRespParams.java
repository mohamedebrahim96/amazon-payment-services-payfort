package com.payfort.fortapisimulator.data.beans;

// Generated Feb 17, 2015 4:19:44 PM by Hibernate Tools 3.4.0.CR1
import java.math.BigInteger;


public class PfFortReqRespParams implements java.io.Serializable {


    private BigInteger id;
    private String paramName;
    private String description;
    private BigInteger paramValueType;
    private BigInteger mandatory;
    private String creationDate;
    private String lastUpdatedDate;
    private BigInteger createdBy;
    private BigInteger lastUpdatedBy;
    private BigInteger status;
    private String paramLength;
    private String possibleValues;
    private String allowedSpecialCharacters;
    private String entityName;
    private String entityFieldName;
    private PfFortReqRespParams pfFortReqRespParams;

    //local field
    private boolean isActive;

    public PfFortReqRespParams() {
    }

    public PfFortReqRespParams(BigInteger id) {
        this.id = id;
    }

    public PfFortReqRespParams(BigInteger id, String paramName, String description, BigInteger paramValueType, BigInteger mandatory, String creationDate, String lastUpdatedDate, BigInteger createdBy, BigInteger lastUpdatedBy, BigInteger status, String paramLength, String possibleValues, String allowedSpecialCharacters, String entityName, String entityFieldName) {
        this.id = id;
        this.paramName = paramName;
        this.description = description;
        this.paramValueType = paramValueType;
        this.mandatory = mandatory;
        this.creationDate = creationDate;
        this.lastUpdatedDate = lastUpdatedDate;
        this.createdBy = createdBy;
        this.lastUpdatedBy = lastUpdatedBy;
        this.status = status;
        this.paramLength = paramLength;
        this.possibleValues = possibleValues;
        this.allowedSpecialCharacters = allowedSpecialCharacters;
        this.entityName = entityName;
        this.entityFieldName = entityFieldName;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigInteger getParamValueType() {
        return paramValueType;
    }

    public void setParamValueType(BigInteger paramValueType) {
        this.paramValueType = paramValueType;
    }

    public BigInteger getMandatory() {
        return mandatory;
    }

    public void setMandatory(BigInteger mandatory) {
        this.mandatory = mandatory;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public BigInteger getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(BigInteger createdBy) {
        this.createdBy = createdBy;
    }

    public BigInteger getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(BigInteger lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public BigInteger getStatus() {
        return status;
    }

    public void setStatus(BigInteger status) {
        this.status = status;
    }

    public String getParamLength() {
        return paramLength;
    }

    public void setParamLength(String paramLength) {
        this.paramLength = paramLength;
    }

    public String getPossibleValues() {
        return possibleValues;
    }

    public void setPossibleValues(String possibleValues) {
        this.possibleValues = possibleValues;
    }

    public String getAllowedSpecialCharacters() {
        return allowedSpecialCharacters;
    }

    public void setAllowedSpecialCharacters(String allowedSpecialCharacters) {
        this.allowedSpecialCharacters = allowedSpecialCharacters;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityFieldName() {
        return entityFieldName;
    }

    public void setEntityFieldName(String entityFieldName) {
        this.entityFieldName = entityFieldName;
    }

    public PfFortReqRespParams getPfFortReqRespParams() {
        return pfFortReqRespParams;
    }

    public void setPfFortReqRespParams(PfFortReqRespParams pfFortReqRespParams) {
        this.pfFortReqRespParams = pfFortReqRespParams;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
