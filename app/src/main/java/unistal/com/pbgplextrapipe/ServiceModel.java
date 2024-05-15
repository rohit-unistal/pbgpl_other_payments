package unistal.com.pbgplextrapipe;

 public class ServiceModel {

    private String id;
    private String service;
    private String description;
    private String amount;
    private String tax;
    private String finalAmount;
    private String serviceType;
    private String status;
    private String createdAt;
    private String updatedAt;
    private String statusDatetime;
    private String taxId;
    ServiceModel(String id,
                 String service,
                 String description,
                 String amount,
                 String tax,
                 String finalAmount,
                 String serviceType,
                 String status,
                 String createdAt,
                 String updatedAt,
                 String statusDatetime,
                 String taxId)
    {
       this.id =id;

       this.service = service;
       this.description = description;
       this.amount = amount;
       this.tax = tax;
       this.finalAmount = finalAmount;
       this.serviceType = serviceType;
       this.status = status;
       this.createdAt =createdAt;
       this.updatedAt = updatedAt;
       this.statusDatetime = statusDatetime;
       this.taxId =taxId;
    }

    public String getId() {
       return id;
    }

    public void setId(String id) {
       this.id = id;
    }

    public String getService() {
       return service;
    }

    public void setService(String service) {
       this.service = service;
    }

    public String getDescription() {
       return description;
    }

    public void setDescription(String description) {
       this.description = description;
    }

    public String getAmount() {
       return amount;
    }

    public void setAmount(String amount) {
       this.amount = amount;
    }

    public String getTax() {
       return tax;
    }

    public void setTax(String tax) {
       this.tax = tax;
    }

    public String getFinalAmount() {
       return finalAmount;
    }

    public void setFinalAmount(String finalAmount) {
       this.finalAmount = finalAmount;
    }

    public String getServiceType() {
       return serviceType;
    }

    public void setServiceType(String serviceType) {
       this.serviceType = serviceType;
    }

    public String getStatus() {
       return status;
    }

    public void setStatus(String status) {
       this.status = status;
    }

    public String getCreatedAt() {
       return createdAt;
    }

    public void setCreatedAt(String createdAt) {
       this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
       return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
       this.updatedAt = updatedAt;
    }

    public String getStatusDatetime() {
       return statusDatetime;
    }

    public void setStatusDatetime(String statusDatetime) {
       this.statusDatetime = statusDatetime;
    }

    public String getTaxId() {
       return taxId;
    }

    public void setTaxId(String taxId) {
       this.taxId = taxId;
    }
    @Override
    public String toString() {
       return service;
    }
 }