package com.example.facebuddy_v20;

public class Contacts {
  String image;
  
  String name;
  
  String status;
  
  String uid;
  
  public Contacts() {}
  
  public Contacts(String paramString1, String paramString2, String paramString3, String paramString4) {
    this.name = paramString1;
    this.image = paramString2;
    this.status = paramString3;
    this.uid = paramString4;
  }
  
  public String getImage() {
    return this.image;
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getStatus() {
    return this.status;
  }
  
  public String getUid() {
    return this.uid;
  }
  
  public void setImage(String paramString) {
    this.image = paramString;
  }
  
  public void setName(String paramString) {
    this.name = paramString;
  }
  
  public void setStatus(String paramString) {
    this.status = paramString;
  }
  
  public void setUid(String paramString) {
    this.uid = paramString;
  }
}


/* Location:              C:\Users\dell\Desktop\Decompilation\dex2jar-2.0\classes-dex2jar.jar!\com\example\facebudduy\Contacts.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */