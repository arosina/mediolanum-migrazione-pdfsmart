package prgm.cedacri.adeguatezza.log;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;

public class UserAccessLog
    extends CommandDataModel {

  // --------------------------------------------------- private bean properties

  private TimestampType accessTime = new TimestampType();
  private StringType userCode = new StringType("");
  private StringType countryCode = new StringType("");
  private StringType channelCode = new StringType("");
  private StringType organizationCode = new StringType("");
  private IntegerType accessType = new IntegerType();
  private IntegerType accessResultCode = new IntegerType();
  private StringType description = new StringType("");
  private StringType logonUserCode = new StringType("");
  private StringType dataUserCode = new StringType("");
  private StringType applicationCode = new StringType("");
  private IntegerType functionCode = new IntegerType();
  private IntegerType commandCode = new IntegerType();
  private IntegerType primaryRoleCode = new IntegerType();
  private StringType additionalRolesCodeList = new StringType("");
  private StringType fwGroupCode = new StringType("");
  private StringType startUrl = new StringType("");
  private StringType clientIp = new StringType("");
  private StringType replicationServer = new StringType("");

  // --------------------------------------------------- getter & setter methods

  public TimestampType getAccessTime() {
    return accessTime;
  }

  public void setAccessTime(TimestampType accessTime) {
    this.accessTime = accessTime;
  }

  public IntegerType getAccessType() {
    return accessType;
  }

  public void setAccessType(IntegerType accessType) {
    this.accessType = accessType;
  }

  public StringType getApplicationCode() {
    return applicationCode;
  }

  public void setApplicationCode(StringType applicationCode) {
    this.applicationCode = applicationCode;
  }

  public StringType getChannelCode() {
    return channelCode;
  }

  public void setChannelCode(StringType channelCode) {
    this.channelCode = channelCode;
  }

  public StringType getClientIp() {
    return clientIp;
  }

  public void setClientIp(StringType clientIp) {
    this.clientIp = clientIp;
  }

  public IntegerType getCommandCode() {
    return commandCode;
  }

  public void setCommandCode(IntegerType commandCode) {
    this.commandCode = commandCode;
  }

  public StringType getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(StringType countryCode) {
    this.countryCode = countryCode;
  }

  public StringType getDataUserCode() {
    return dataUserCode;
  }

  public void setDataUserCode(StringType dataUserCode) {
    this.dataUserCode = dataUserCode;
  }

  public StringType getDescription() {
    return description;
  }

  public void setDescription(StringType description) {
    this.description = description;
  }

  public IntegerType getFunctionCode() {
    return functionCode;
  }

  public void setFunctionCode(IntegerType functionCode) {
    this.functionCode = functionCode;
  }

  public StringType getFwGroupCode() {
    return fwGroupCode;
  }

  public void setFwGroupCode(StringType fwGroupCode) {
    this.fwGroupCode = fwGroupCode;
  }

  public StringType getLogonUserCode() {
    return logonUserCode;
  }

  public void setLogonUserCode(StringType logonUserCode) {
    this.logonUserCode = logonUserCode;
  }

  public StringType getOrganizationCode() {
    return organizationCode;
  }

  public void setOrganizationCode(StringType organizationCode) {
    this.organizationCode = organizationCode;
  }

  public StringType getStartUrl() {
    return startUrl;
  }

  public void setStartUrl(StringType startUrl) {
    this.startUrl = startUrl;
  }

  public StringType getUserCode() {
    return userCode;
  }

  public void setUserCode(StringType userCode) {
    this.userCode = userCode;
  }

  public StringType getAdditionalRolesCodeList() {
    return additionalRolesCodeList;
  }

  public void setAdditionalRolesCodeList(StringType additionalRolesCodeList) {
    this.additionalRolesCodeList = additionalRolesCodeList;
  }

  public IntegerType getPrimaryRoleCode() {
    return primaryRoleCode;
  }

  public void setPrimaryRoleCode(IntegerType primaryRoleCode) {
    this.primaryRoleCode = primaryRoleCode;
  }

  public IntegerType getAccessResultCode() {
    return accessResultCode;
  }

  public void setAccessResultCode(IntegerType accessResultCode) {
    this.accessResultCode = accessResultCode;
  }

  public StringType getReplicationServer() {
    return replicationServer;
  }

  public void setReplicationServer(StringType replicationServer) {
    this.replicationServer = replicationServer;
  }

}