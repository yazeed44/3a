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
    private final String mStorageSpace;
    private final String mMonthlyPackageOffer;
    public final String emailsCount;
    private final int mYearCost;
    public final int id;
    
    public static final String UNIT_MB = "MB";
    
    private HostingPackage(final Builder builder){
        name = builder.mName;
        mStorageSpace = builder.mStorageSpace;
        mMonthlyPackageOffer = builder.mMonthlyPackageOffer;
        emailsCount = builder.mEmailsCount;
        mYearCost = builder.mYearCost;
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
        final HostingPackage other = (HostingPackage) obj;
        return this.id == other.id;
    }
    
    public String getMonthlyPackageOfferWithUnit(){
        if (!isStringValid(mMonthlyPackageOffer)){
            return null;
        }
        
        else {
            return mMonthlyPackageOffer + " " + UNIT_MB;
        }
        
        
    }
    
    public String getMonthlyPackageOfferWithoutUnit(){
        if (!isStringValid(mMonthlyPackageOffer)){
            return null;
        }
        
        else {
            return mMonthlyPackageOffer;
        }
    }
    
    public String getStorageSpaceWithUnit(){
        if (!isStringValid(mStorageSpace)){
            return null;
        }
        
        else {
            return mStorageSpace + " " + UNIT_MB;
        }
    }
    
    public String getStorageSpaceWithoutUnit(){
        if (!isStringValid(mStorageSpace)){
            return null;
        }
        
        else {
            return mStorageSpace;
        }
    }
    
    
    private boolean isStringValid(final String s){
        return s != null && s.length() > 0;
    }
    
    public String getYearCostWithUnit(){
        return mYearCost + " "+Order.CURRENCY;
    }
    
    public String getYearCostWithoutUnit(){
        return mYearCost + "";
    }
    
    public int getYearCostAsInteger(){
        return mYearCost;
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
