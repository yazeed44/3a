/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3a.utils;

/**
 *
 * @author yazeed44
 */
public class Customer {
    
    public static final String EMPTY = "لايوجد";
    
    public static final Customer EMPTY_CUSTOMER = new Builder(EMPTY,-1)
            .setAddress(EMPTY)
            .setCompany(EMPTY)
            .setMobileNumber(EMPTY)
            .setPhoneNumber(EMPTY)
            .build();
    
    public final String name;
    public final String mobileNumber;
    public final String company;
    public final String phoneNumber;
    public final String address;
    public final int id;
    private Customer(final Builder builder){
        name = builder.mName;
        mobileNumber = builder.mMobileNumber;
        company = builder.mCompany;
        phoneNumber = builder.mPhoneNumber;
        address = builder.mAddress;
        id = builder.mId;
        
    }

    @Override
    public String toString() {
        
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Customer other = (Customer) obj;
        return this.id == other.id;
    }
    
    
    
    
   
    
    public static class Builder {
        
        private final String mName;
        private String mMobileNumber;
        private String mCompany = EMPTY;
        private String mPhoneNumber;
        private String mAddress;
        private final int mId;
        
        public Builder(final String name,final int id){
            mName = name;
            mId = id;
        }
        
        public Builder setMobileNumber(final String mobileNumber){
            this.mMobileNumber = mobileNumber;
            return this;
        }
        
        public Builder setCompany(final String company){
            this.mCompany = company;
            return this;
        }
        
        public Builder setPhoneNumber(final String phoneNumber){
            mPhoneNumber = phoneNumber;
            return this;
        }
        
        public Builder setAddress(final String address){
            mAddress = address;
            return this;
        }
        
        public Customer build(){
            return new Customer(this);
        }
    }
    
}
