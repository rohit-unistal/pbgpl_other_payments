package unistal.com.pbgplextrapipe;

class ModificationServiceModel {


      private String id;
      private String serviceType;
      private String dateOfRequest;
      private String message;
      private String file;
      private String status;
      private String requestNumber;
      private String dmaId;
      private String maintenanceType;
      private String serviceName;
   private String bp_number;
   public ModificationServiceModel(String id,
          String serviceType,
          String dateOfRequest,
          String message,
          String file ,

          String status,
          String requestNumber,
          String dmaId,
         String maintenanceType,
         String serviceName,
         String bp_number)
   {
      this.id= id;this.serviceType = serviceType;this.dateOfRequest = dateOfRequest;
      this.message = message; this.file = file;this.status = status;
      this.requestNumber =requestNumber;this.dmaId = dmaId;
      this.maintenanceType = maintenanceType; this.serviceName = serviceName;
      this.bp_number = bp_number;
   }



   public String getId() {
         return id;
      }

      public void setId(String id) {
         this.id = id;
      }

      public String getServiceType() {
         return serviceType;
      }

      public void setServiceType(String serviceType) {
         this.serviceType = serviceType;
      }

      public String getDateOfRequest() {
         return dateOfRequest;
      }

      public void setDateOfRequest(String dateOfRequest) {
         this.dateOfRequest = dateOfRequest;
      }

      public String getMessage() {
         return message;
      }

      public void setMessage(String message) {
         this.message = message;
      }

      public Object getFile() {
         return file;
      }

      public void setFile(String file) {
         this.file = file;
      }

      public String getStatus() {
         return status;
      }

      public void setStatus(String status) {
         this.status = status;
      }

      public String getRequestNumber() {
         return requestNumber;
      }

      public void setRequestNumber(String requestNumber) {
         this.requestNumber = requestNumber;
      }

      public String getDmaId() {
         return dmaId;
      }

      public void setDmaId(String dmaId) {
         this.dmaId = dmaId;
      }

      public String getMaintenanceType() {
         return maintenanceType;
      }

      public void setMaintenanceType(String maintenanceType) {
         this.maintenanceType = maintenanceType;
      }

      public String getServiceName() {
         return serviceName;
      }

      public void setServiceName(String serviceName) {
         this.serviceName = serviceName;
      }
   public String getBp_number() {
      return bp_number;
   }

   public void setBp_number(String bp_number) {
      this.bp_number= bp_number;
   }





}
