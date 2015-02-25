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
public class HostingPackage {
    
    public static final String EMPTY = "لايوجد";
    
    public final static HostingPackage EMPTY_PACKAGE = new Builder(EMPTY,-1)
            .setEmailsCount(EMPTY)
            .setMonthlyPackageOffer(EMPTY)
            .setStorageSpace(EMPTY)
            .setYearCost(0)
            .build();
    
    public final String name;
    public final String storageSpace;
    public final String monthlyPackageOffer;
    public final String emailsCount;
    public final int yearCost;
    public final int id;
    
    private HostingPackage(final Builder builder){
        name = builder.mName;
        storageSpace = builder.mStorageSpace;
        monthlyPackageOffer = builder.mMonthlyPackageOffer;
        emailsCount = builder.mEmailsCount;
        yearCost = builder.mYearCost;
        id = builder.mId;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HostingPackage other = (HostingPackage) obj;
        return this.id == other.id;
    }
    
    
    
    
    
    public static class Builder {
        private final String mName;
        private String mStorageSpace;
        private String mMonthlyPackageOffer;
        private String mEmailsCount;
        private int mYearCost;
        private int mId;
        
        public Builder (final String name , final int id){
            mName = name;
            mId = id;
        }
        
        public Builder setStorageSpace(final String storageSpace){
            mStorageSpace = storageSpace;
            return this;
        }
        
        public Builder setMonthlyPackageOffer(final String offer){
            mMonthlyPackageOffer = offer;
            return this;
        }
        
        public Builder setEmailsCount(final String emailsCount){
            mEmailsCount = emailsCount;
            return this;
        }
        
        public Builder setYearCost(final int yearCost){
            mYearCost = yearCost;
            return this;
        }
        
        
        
        public HostingPackage build(){
            return new HostingPackage(this);
        }
        
    }
}
