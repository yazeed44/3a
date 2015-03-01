/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3a.utils;

import java.sql.Date;

import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import org.joda.time.DateTime;
import org.joda.time.Days;
import pkg3a.utils.DBUtil.QueryDbListener;

/**
 *
 * @author yazeed44
 */
public final class Order implements QueryDbListener<Customer>  {
    
    public static final String EMPTY = "لايوجد";
    
    public static final Order EMPTY_ORDER = new Builder(-1,-1)
            .setBeginingDate(-1)
            .setDomain(EMPTY)
            .setDomainCost(-1)
            .setDomainEndDate(-1)
            .setHostingPackage(-1)
            .setTotalCost(-1)
            .build();
            
            
            ;
    
    public static final String NO_DOMAIN = "noDomain";
    
    private final String mDomain;
    public int hostingPackageId;
     final int mCustomerId;
    private final Date mBeginingDate; 
    private final int totalCost;
    public final int domainCost;
    private final Date mDomainExpireDate;
    public final int id;
    private  Customer mCustomer;
    public final String note;
    private boolean mIsActivated;
    private final Date mHostingPackageExpireDate;
    
    public static final String CURRENCY = "SR";
    
    
    
    
    
    private Order(final Builder builder){
        id = builder.mId;
        mCustomerId = builder.mCustomerId;
        hostingPackageId = builder.mPackageId;
        mDomain = builder.mDomain;
        mBeginingDate = builder.mBeginingDate;
        totalCost = builder.mTotalCost;
        domainCost = builder.mDomainCost;
        mDomainExpireDate = builder.mDomainExpireDate;
        note = builder.mNote;
        mIsActivated = builder.mIsActivated;
        mHostingPackageExpireDate = builder.mHostingPackageExpireDate;
        
        
        if (mCustomerId != -1){
            System.out.println("Started loading customer for  " + toString() + "");
        DBUtil.loadCustomer(mCustomerId,this);
        
        }
    }
    
    public boolean domainExists(){
        
        return mDomain != null && mDomain.length() > 0 && !mDomain.equals(NO_DOMAIN) && !mDomain.equals(EMPTY);
        
    }
    
    public String getDomain(){
        
        if (domainExists()){
            return mDomain;
        }
        else {
            return EMPTY;
        }
        
    }
        public boolean hostingPackageExists(){
            return hostingPackageId != -1;
            
        }
        public void loadHostingPackage(DBUtil.QueryDbListener<HostingPackage> listener){
            
            if (hostingPackageExists()){
                //TODO 
                DBUtil.loadHostingPackage(hostingPackageId, listener);
            }
            
            else {
                listener.failedToQuery(new Exception("No hosting package for " + this.toString()));
            }
            
        }
        public String getBeginingDateFormatted(){
             return mBeginingDate.toLocalDate().format(DateTimeFormatter.ISO_DATE);
            
        }
        
        public String getEndDomainDateFormatted(){
            
            if (domainExists()){
             return mDomainExpireDate.toLocalDate().format(DateTimeFormatter.ISO_DATE);
             
            }
            else {
                return NO_DOMAIN;
            }
            
           
        }
        
        public String getTotalCostWithCurrency(){
            return totalCost + CURRENCY;
        }
        
        public String getTotalCostWithoutCurrency(){
            return totalCost + "";
        }
        
        public long getBeginingDateMillis(){
            return mBeginingDate.getTime();
            
        }
        
        public long getEndDomainDateMillis(){
            
            if (mDomainExpireDate == null){
                return -1;
            }
            else {
                return mDomainExpireDate.getTime();
            }
            
            
        }
        
        public long getHostingPackageEndDateMillis(){
            if (mHostingPackageExpireDate == null){
                return -1;
            }
            
            else {
                return mHostingPackageExpireDate.getTime();
            }
        }
        
        public boolean isDomainNearExpire(){
            
            if (!domainExists() || !isActivated()){
                return false;
            }
            
            final java.util.Date now = Calendar.getInstance().getTime();
            
            final DateTime nowTime = new DateTime(now),expireTime = new DateTime(mDomainExpireDate);
            Days daysBetweenNowAndExpire = Days.daysBetween(nowTime,expireTime);
            
            int days = daysBetweenNowAndExpire.getDays();
            
            return days <= 30 && days > 0;
        }
        
        public boolean hasDomainExpired(){
            if (!domainExists() || !isActivated()){
                return false;
            }  
            
            final java.util.Date now = Calendar.getInstance().getTime();
            return mDomainExpireDate.before(now) || mDomainExpireDate.equals(now) ;
        }
        
        public boolean isNoteValid(){
        return note != null && note.length() > 0;
        }
        
        public boolean isActivated(){
            return mIsActivated;
        }
        
        public void setActivated(final boolean state){
            mIsActivated = state;
        }
        
       

    @Override
    public String toString() {
        return "Order{" + "mDomain=" + mDomain + ", customerId=" + mCustomerId + '}';
    }
    
    public Customer getCustomer(){
        return mCustomer;
    }

    @Override
    public void queriedSuccessfully(Customer result) {
        mCustomer = result;
    }

    @Override
    public void failedToQuery(Throwable throwable) {
        
    }


    public static final class Builder {
        
        private final int mCustomerId;
        private String mDomain = NO_DOMAIN;
        private int mPackageId = -1;
        private Date mBeginingDate;
        private Date mDomainExpireDate;
        private int mTotalCost;
        private int mDomainCost = 0;
        private final int mId;
        private String mNote;
        private boolean mIsActivated = true;
        private Date mHostingPackageExpireDate;
        

        public Builder(final int customerId , final int id) {
            
            mCustomerId = customerId;
            mId = id;
        }
        
        public Builder(final int customerId){
            mCustomerId = customerId;
            mId = -1; //Because this constructur is used for insert only !!
        }
        
        public Builder (final Order order){
            mCustomerId = order.mCustomerId;
            mId = order.id;
            setDomain(order.getDomain())
                    .setBeginingDate(order.getBeginingDateMillis())
                    .setDomainCost(order.domainCost)
                    .setDomainEndDate(order.getEndDomainDateMillis())
                    .setHostingPackage(order.hostingPackageId)
                    .setNote(order.note)
                    .setTotalCost(order.totalCost)
                    .setHostingPackageEndDate(order.getHostingPackageEndDateMillis())
                    .isActivated(order.isActivated())
                    ;
                    
        }
        
        public Builder setDomain(final String domain){
            mDomain = domain;
            return this;
        }
        
        public Builder setHostingPackage(final int hostingPacakgeId){
            mPackageId = hostingPacakgeId;
            return this;
        }
        
        public Builder setBeginingDate(final long beginingDateMillis){      
            mBeginingDate = new Date(beginingDateMillis);      
            return this;
            
        }
        
        public Builder setDomainEndDate(final long domainEndDateMillis){
            mDomainExpireDate = new Date(domainEndDateMillis);
            return this;
        }
        
        public Builder setTotalCost(final int cost){
            mTotalCost = cost;
            return this;
        }
        
        public Builder setDomainCost(final int domainCost){
            mDomainCost = domainCost;
            return this;
        }
        
        public Builder setNote(final String note){
            mNote = note;
            return this;
        }
        
        public Builder isActivated(final boolean activated){
            mIsActivated = activated;
            return this;
        }
        
        public Builder setHostingPackageEndDate(final long endMillis){
            mHostingPackageExpireDate = new Date(endMillis);
            return this;
        }
        
        
        
        public Order build(){
            return new Order(this);
        }
        
    }
    
    
}
