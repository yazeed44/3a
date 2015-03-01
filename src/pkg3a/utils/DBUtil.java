/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3a.utils;

import com.almworks.sqlite4java.SQLParts;
import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteJob;
import com.almworks.sqlite4java.SQLiteQueue;
import com.almworks.sqlite4java.SQLiteStatement;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import static pkg3a.utils.DBConstants.*;

/**
 *
 * @author yazeed44
 */
public final class DBUtil {
    
    
    private static File dbFile;
    
    


            
            
    private static void initializeDb(){
        
        if (dbFile == null){
            dbFile = new File(DB_NAME);
            System.out.println("Db file initialized " + dbFile.getAbsolutePath());
        }
        
        
    }
    

    public static void insertHostingPackage(final HostingPackage hostingPackage
            ,final InsertDbListener listener){
        initializeDb();
        
        
         execute(new InsertJob(listener){

            @Override
            protected Object job(SQLiteConnection connection) throws Throwable {
            final SQLParts sqlParts = new SQLParts();
                
                sqlParts.append("INSERT INTO ")
                .append(TABLE_HOSTING_PACKAGES)
                .append("(name, storageSpace, monthlyOffer, emailsCount, yearCost ) ")
                .append("VALUES(")
                .appendParams(5)
                .append(");");
                
                System.out.println("Statement is  " + sqlParts.toString());
                
                final SQLiteStatement st = connection.prepare(sqlParts);
                
                bindHostingPackage(st,hostingPackage);
                
                
                
                stepAndDispose(st);
                
                
                return new Object();
            }
        });
        

        
    }
    
    private static void bindHostingPackage(final SQLiteStatement st , final HostingPackage hostingPackage) throws SQLiteException{
        st.bind(COLUMN_NAME_INDEX, hostingPackage.name)
                        .bind(COLUMN_YEAR_COST_INDEX, hostingPackage.getYearCostAsInteger())
                        .bind(COLUMN_STORAGE_SPACE_INDEX, hostingPackage.getStorageSpaceWithoutUnit())
                        .bind(COLUMN_MONTHLY_OFFER_INDEX, hostingPackage.getMonthlyPackageOfferWithoutUnit())
                        .bind(COLUMN_EMAILS_COUNT_INDEX, hostingPackage.emailsCount);
    }
    
    private static void execute(SQLiteJob job){
        final SQLiteQueue queue = new SQLiteQueue(dbFile);
        queue.start();
        queue.execute(job);
        try {
            queue.stop(true).join();
        } catch (InterruptedException ex) {
            Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        }
    }
    
    private static void stepAndDispose(final SQLiteStatement st) throws SQLiteException{
        
        try {
            st.step();
        }
        finally {
            st.dispose();
        }
    }
    
    public static void insertCustomer(Customer customer ,
            InsertDbListener listener){
        initializeDb();
        
        execute(new InsertJob(listener){

            @Override
            protected Object job(SQLiteConnection connection) throws Throwable {
                
                final SQLParts parts = new SQLParts();
                
                parts.append("INSERT INTO ")
                        .append(DBConstants.TABLE_CUSTOMERS)
                        .append("(name,mobileNumber,companyName,phoneNumber,address) ")
                        .append("VALUES (")
                        .appendParams(5)
                        .append(");")
                        ;
                
                System.out.println("Insert statement is  " + parts.toString());
                
                final SQLiteStatement st = connection.prepare(parts);
                
                
                bindCustomer(st,customer);
                
                
                stepAndDispose(st);
                
                
                
                return new Object();
            }
        });
        
        
        
    }
    
    private static void bindCustomer(final SQLiteStatement st,final Customer customer) throws SQLiteException{
        st.bind(DBConstants.COLUMN_NAME_INDEX, customer.name)
                        .bind(DBConstants.COLUMN_MOBILE_NUMBER_INDEX, customer.mobileNumber)
                        .bind(DBConstants.COLUMN_COMPANY_NAME_INDEX, customer.company)
                        .bind(DBConstants.COLUMN_PHONE_NUMBER_INDEX, customer.phoneNumber)
                        .bind(DBConstants.COLUMN_ADDRESS_INDEX, customer.address)
                        ;
    }
    
    public static void insertOrder(Order order ,  InsertDbListener listener){
        initializeDb();
        
        execute(new InsertJob(listener){

            
            @Override
            protected Object job(SQLiteConnection connection) throws Throwable {
                
                final SQLParts parts = new SQLParts();
                
                parts.append("INSERT INTO ")
                        .append(DBConstants.TABLE_ORDERS)
                        .append("(domain,beginingDate,totalCost,domainEndDate,domainCost,customer_id,hostingPackage_id,note,isActivated,hostingPackageEndDate) ")
                        .append("VALUES (")
                        .appendParams(10)
                        .append(");");
                
                System.out.println("The statment is  " + parts.toString());    
                
                final SQLiteStatement st = connection.prepare(parts);
                
                bindOrder(st,order);
               
                
                stepAndDispose(st);
                
                return new Object();
            }
        });
        
    }
    
    private static void bindOrder(final SQLiteStatement st , final Order order) throws SQLiteException{
         st.bind(COLUMN_DOMAIN_INDEX, order.domainExists() ? order.getDomain() : null)
                        .bind(COLUMN_BEGINING_DATE_INDEX, order.getBeginingDateMillis())
                        .bind(COLUMN_TOTAL_COST_INDEX, order.getTotalCostWithoutCurrency())
                        .bind(COLUMN_DOMAIN_END_DATE_INDEX, order.getEndDomainDateMillis() == -1 ? null:order.getEndDomainDateMillis() + "")
                        .bind(COLUMN_DOMAIN_COST_INDEX, order.domainCost == 0 ? null:order.domainCost+"")
                        .bind(COLUMN_CUSTOMER_ID_INDEX, order.mCustomerId)
                        .bind(COLUMN_HOSTING_PACKAGE_ID_INDEX, order.hostingPackageId == -1 ? null:order.hostingPackageId+"")
                        .bind(COLUMN_NOTE_INDEX,order.isNoteValid() ? order.note : null)
                        .bind(COLUMN_IS_ACTIVATED_INDEX, order.isActivated() ? 1:0)
                        .bind(COLUMN_HOSTING_PACKAGE_END_DATE_INDEX, order.getHostingPackageEndDateMillis() == -1 ? null:order.getHostingPackageEndDateMillis()+ "")
                 ;
    }
    
    
    
    public static void loadHostingPackage(final int id , QueryDbListener<HostingPackage> listener){
        initializeDb();
        
        execute(new QueryJob(listener){

            @Override
            protected Object job(SQLiteConnection connection) throws Throwable {
                
                final SQLiteStatement st = connection.prepare("SELECT * FROM " + TABLE_HOSTING_PACKAGES
                + " WHERE _id = " + id);
                
                st.step();
                
                final HostingPackage result = getHostingPackage(st);
                
                st.dispose();
                return result;
                
                
            }
        });

    }
    
    
    private static HostingPackage getHostingPackage(final SQLiteStatement st) throws SQLiteException{
        return new HostingPackage.Builder(st.columnString(COLUMN_NAME_INDEX), st.columnInt(COLUMN_ID_INDEX))
                        .setStorageSpace(st.columnString(COLUMN_STORAGE_SPACE_INDEX))
                        .setMonthlyPackageOffer(st.columnString(COLUMN_MONTHLY_OFFER_INDEX))
                        .setEmailsCount(st.columnString(COLUMN_EMAILS_COUNT_INDEX))
                        .setYearCost(st.columnInt(COLUMN_YEAR_COST_INDEX))
                        .build();
    }
    
    public static void loadHostingPackages(final QueryDbListener<ArrayList<HostingPackage>> listener){
        initializeDb();
        
        execute(new QueryJob(listener){

            @Override
            protected Object job(SQLiteConnection connection) throws Throwable {
                final ArrayList<HostingPackage> packages = new ArrayList<>();
                
                final SQLiteStatement st = connection.prepare("SELECT * FROM " + TABLE_HOSTING_PACKAGES);
                
                while(st.step()){
                    packages.add(getHostingPackage(st));
                }
                
                st.dispose();
                
                return packages;
                
            }
        });
    }
    

    
    
    public static void loadCustomer(final int id , QueryDbListener<Customer> listener){
        initializeDb();
        
        execute(new QueryJob(listener){

            @Override
            protected Object job(SQLiteConnection connection) throws Throwable { 
                        final SQLiteStatement st = connection.prepare("SELECT * FROM " +
                                DBConstants.TABLE_CUSTOMERS
                        + " WHERE _id = " + id);
                
                st.step();
                
                final Customer result = getCustomer(st);
                
                st.dispose();
                
                return result;
                
            }
        });
        
    }
    
    private static Customer getCustomer(final SQLiteStatement st) throws SQLiteException{
        return new Customer.Builder(st.columnString(COLUMN_NAME_INDEX), st.columnInt(COLUMN_ID_INDEX))
                        .setMobileNumber(st.columnString(COLUMN_MOBILE_NUMBER_INDEX))
                        .setCompany(st.columnString(COLUMN_COMPANY_NAME_INDEX))
                        .setPhoneNumber(st.columnString(COLUMN_PHONE_NUMBER_INDEX))
                        .setAddress(st.columnString(COLUMN_ADDRESS_INDEX))
                        .build();
    }
    
    public static void loadCustomers(final QueryDbListener<ArrayList<Customer>> listener){
        initializeDb();
        
        execute(new QueryJob(listener){

            @Override
            protected Object job(SQLiteConnection connection) throws Throwable {
                final SQLiteStatement st = connection.prepare("SELECT * FROM " + TABLE_CUSTOMERS);
                
                final ArrayList<Customer> customers = new ArrayList();
                
                while(st.step()){
                    customers.add(getCustomer(st));
                }
                
                st.dispose();
                
                return customers;
                
            }
        });
    }
    
    
  
    
    
    public static void loadOrder(final int id , QueryDbListener<Order> listener){
        initializeDb();
        
        execute(new QueryJob(listener){

            @Override
            protected Object job(SQLiteConnection connection) throws Throwable {
                
                final SQLiteStatement st = connection.prepare("SELECT * FROM " 
                        + DBConstants.TABLE_ORDERS + " WHERE _id = " + id);
                
                st.step();
                
                final Order result = getOrder(st);
                        
                
                st.dispose();
                
                return result;
                        
                
            }
        });
        
    }
    
    
    private static Order getOrder(final SQLiteStatement st) throws SQLiteException{
        
        
        return new Order.Builder(st.columnInt(COLUMN_CUSTOMER_ID_INDEX),st.columnInt(COLUMN_ID_INDEX))
                        .setBeginingDate(st.columnLong(COLUMN_BEGINING_DATE_INDEX))
                        .setDomain(st.columnString(COLUMN_DOMAIN_INDEX))
                        .setDomainCost(st.columnInt(COLUMN_DOMAIN_COST_INDEX))
                        .setDomainEndDate(st.columnLong(COLUMN_DOMAIN_END_DATE_INDEX))
                        .setHostingPackage(st.columnInt(COLUMN_HOSTING_PACKAGE_ID_INDEX) == 0 ? -1:st.columnInt(COLUMN_HOSTING_PACKAGE_ID_INDEX))
                        .setTotalCost(st.columnInt(COLUMN_TOTAL_COST_INDEX))
                        .setNote(st.columnString(COLUMN_NOTE_INDEX))
                        .isActivated(st.columnInt(COLUMN_IS_ACTIVATED_INDEX) == 1)
                        .setHostingPackageEndDate(st.columnLong(COLUMN_HOSTING_PACKAGE_END_DATE_INDEX) == 0 ? -1:st.columnLong(COLUMN_HOSTING_PACKAGE_END_DATE_INDEX))
                        .build();
    }
    
    public static void loadOrders(final QueryDbListener<ArrayList<Order>> listener){
        initializeDb();
        
        execute(new QueryJob(listener){

            @Override
            protected Object job(SQLiteConnection connection) throws Throwable {
                final SQLiteStatement st = connection.prepare("SELECT * FROM " + TABLE_ORDERS
                +" ORDER BY beginingDate DESC"  );
                
                final ArrayList<Order> orders = new ArrayList<>();
                
                while(st.step()){
                    orders.add(getOrder(st));
                }
                
                st.dispose();
                
                return orders;
            }
        });
    }
    
    public static void loadOrdersWithinRange(final long fromMillis,final long toMillis
    ,final QueryDbListener<ArrayList<Order>> listener){
    
        initializeDb();
        
        execute(new QueryJob(listener){

            @Override
            protected Object job(SQLiteConnection connection) throws Throwable {
                final String sql = "SELECT * FROM " + TABLE_ORDERS + " WHERE domainEndDate <= "+ toMillis
                        +" AND domainEndDate >= " + fromMillis +" AND isActivated = 1" ;
                
                System.out.println("Query orders with range statement : " + sql);
                
                final SQLiteStatement st = connection.prepare(sql);
                
                final ArrayList<Order> orders = new ArrayList<>();
                
                while(st.step()){
                    orders.add(getOrder(st));
                }
                st.dispose();
                
                return orders;
                        
            }
        });
    }
    
    public static void loadOrdersFromCustomer(final int customerId , final QueryDbListener<ArrayList<Order>> listener){
    initializeDb();
    
    execute(new QueryJob(listener){

        @Override
        protected Object job(SQLiteConnection connection) throws Throwable {
            final String sql = "SELECT * FROM " + TABLE_ORDERS + " WHERE customer_id = " + customerId;
            
            final SQLiteStatement st = connection.prepare(sql);
            
            final ArrayList<Order> orders = new ArrayList<>();
            
            while(st.step()){
                orders.add(getOrder(st));
            }
            st.dispose();
            
            return orders;
        }
    });
    }
    
    public static void loadOrdersFromHostingPackage(final int hostingPackageId ,
            final QueryDbListener<ArrayList<Order>> listener){
        initializeDb();
        
        execute(new QueryJob(listener){

            @Override
            protected Object job(SQLiteConnection connection) throws Throwable {
                
                final String sql = "SELECT * FROM " + TABLE_ORDERS + " WHERE hostingPackage_id = " + hostingPackageId;
                
                final SQLiteStatement st = connection.prepare(sql);
                
                final ArrayList<Order> orders = new ArrayList<>();
                
                while(st.step()){
                    orders.add(getOrder(st));
                }
                
                st.dispose();
                
                return orders;
            }
        });
    }
    
    public static void updateHostingPackage(final HostingPackage toUpdatePackage
            , final UpdateDbListener<HostingPackage> updateResultListener){
        initializeDb();
        
        execute(new UpdateJob(updateResultListener){

            @Override
            protected Object job(SQLiteConnection connection) throws Throwable {
                
                final String statement = "UPDATE " + TABLE_HOSTING_PACKAGES 
                        +" SET name=? , storageSpace=? , monthlyOffer=? , emailsCount=? , yearCost=?"
                        +" WHERE _id = " + toUpdatePackage.id + ";" 
                        ;
                        
                        final SQLiteStatement st = connection.prepare(statement);
                        
                        bindHostingPackage(st,toUpdatePackage);
                        stepAndDispose(st);
                        
                        return toUpdatePackage;
                        
            }
        });
        
    }
    
    
    
    public static void updateCustomer(final Customer toUpdateCustomer 
            ,final UpdateDbListener<Customer> updateResultListener){
        initializeDb();
        
        execute(new UpdateJob(updateResultListener){

            @Override
            protected Object job(SQLiteConnection connection) throws Throwable {
                final String statement = "UPDATE " + TABLE_CUSTOMERS + " SET name=? , "
                        +"mobileNumber=? , companyName=? , phoneNumber=? , address=? " 
                        + "WHERE _id = " + toUpdateCustomer.id + ";";
                        ;
                        
                        final SQLiteStatement st = connection.prepare(statement);
                        
                        bindCustomer(st,toUpdateCustomer);
                        stepAndDispose(st);
                        
                        
                
                
                return toUpdateCustomer;
            }
        });
    }
    
    public static void updateOrder(final Order toUpdateOrder
            ,final UpdateDbListener<Order> updateResultListener){
        initializeDb();
        
        execute(new UpdateJob(updateResultListener){

            @Override
            protected Object job(SQLiteConnection connection) throws Throwable {
                
                final String statement = "UPDATE " + TABLE_ORDERS + " SET domain=?"
                        +" ,beginingDate=? , totalCost=? , domainEndDate=? , domainCost=? , "
                        + "customer_id=? , hostingPackage_id =? , note=? , isActivated=? ,hostingPackageEndDate=? WHERE _id = " + toUpdateOrder.id +";" 
                        ;
                
                final SQLiteStatement st = connection.prepare(statement);
                
                bindOrder(st,toUpdateOrder);
                stepAndDispose(st);
                
                return toUpdateOrder;
                
            }
        });
    }
    
    public static void deleteHostingPackage(final int id
            ,final DeleteDbListener deleteResultListener){
        initializeDb();
        
        execute(new DeleteJob(deleteResultListener){

            @Override
            protected Object job(SQLiteConnection connection) throws Throwable {
                
                final String statement = "DELETE FROM " + TABLE_HOSTING_PACKAGES
                        +" WHERE _id = "+ id;
                
                applyDelete(connection,statement);
                
                return new Object();
            }
        });
        
    }
    private static void applyDelete(final SQLiteConnection connection , final String statement) throws SQLiteException
    {
        final SQLiteStatement st = connection.prepare(statement);
        stepAndDispose(st);
                
    }
    
    public static void deleteCustomer(final int id,final DeleteDbListener deleteResultListener){
        initializeDb();
        
        execute(new DeleteJob(deleteResultListener){

            @Override
            protected Object job(SQLiteConnection connection) throws Throwable {
                
                final String statement = "DELETE FROM " + TABLE_CUSTOMERS +" WHERE " 
                        + "_id = " + id;
                
                applyDelete(connection,statement);
                
                return new Object();
            }
        });
    }
    
    public static void deleteOrder(final int id,final DeleteDbListener deleteResultListener){
        initializeDb();
        
        execute(new DeleteJob(deleteResultListener){

            @Override
            protected Object job(SQLiteConnection connection) throws Throwable {
                
                final String statement = "DELETE FROM " + TABLE_ORDERS +" WHERE _id = " + id;
                applyDelete(connection,statement);
                
                
                return new Object();
            }
        });
    }
    
    public static void deleteOrdersRelatedToCustomer(final int customerId , final DeleteDbListener listener){
        initializeDb();
        
        execute(new DeleteJob(listener){

            @Override
            protected Object job(SQLiteConnection connection) throws Throwable {
                final String sql = "DELETE FROM " + TABLE_ORDERS + " WHERE customer_id = " + customerId;
                
                final SQLiteStatement st = connection.prepare(sql);
                
                
                
                while(st.step()){
                    
                }
                
                return new Object();
                
            }
        });
    }
    
    public static void deleteOrdersRelatedToHostingPackages(final int hostingPackageId,final DeleteDbListener listener){
        initializeDb();
        
        execute(new DeleteJob(listener){

            @Override
            protected Object job(SQLiteConnection connection) throws Throwable {
                
                final String sql = "DELETE FROM " + TABLE_ORDERS + " WHERE hostingPackage_id = " + hostingPackageId;
                
                final SQLiteStatement st = connection.prepare(sql);
                
                while(st.step()){
                    
                }
                
                return new Object();
            }
        });
        
    }
    
    
    
    
    private static abstract class InsertJob extends SQLiteJob{
        
        private final InsertDbListener mListener;
        private InsertJob(final InsertDbListener listener){
            mListener = listener;
        }

        @Override
        protected void jobError(Throwable error) throws Throwable {
            super.jobError(error); 
            if (mListener != null)
                mListener.failedToInsert(error);
            
            System.out.println("Insert error " + error.getMessage());
        }

        @Override
        protected void jobFinished(Object result) throws Throwable {
            super.jobFinished(result); 
            if (result != null && mListener != null)
                mListener.insertedSuccessfully();
            
            
        }
        
    }
    
    private static abstract class QueryJob extends SQLiteJob{
        
        private QueryDbListener mListener;
        private QueryJob(final QueryDbListener listener){
            mListener = listener;
        }
        
        @Override
        protected void jobError(Throwable error) throws Throwable {
            super.jobError(error); 
            if (mListener != null)
                mListener.failedToQuery(error);
            
            System.out.println("Query error " + error.getMessage());
        }

        @Override
        protected void jobFinished(Object result) throws Throwable {
            super.jobFinished(result); 
            if (result != null && mListener != null)
                mListener.queriedSuccessfully(result);
        }
        
    }
    
    private static abstract class UpdateJob extends SQLiteJob{
        private UpdateDbListener mListener;
        
        private UpdateJob(final UpdateDbListener listener){
            mListener = listener;
        }
        
        @Override
        protected void jobError(Throwable error) throws Throwable {
            super.jobError(error); 
            if (mListener != null)
                mListener.failedToUpdate(error);
            
            System.out.println("Update error " + error.getMessage());
        }

        @Override
        protected void jobFinished(Object result) throws Throwable {
            super.jobFinished(result); 
            if (result != null && mListener != null){
                if (result instanceof HostingPackage){
                    loadHostingPackage(result);
                }
                
                else if (result instanceof Customer){
                    loadCustomer(result);
                }
                
                else if (result instanceof Order){
                    loadOrder(result);
                }
                
            }
            
        }
        
        private void loadHostingPackage(Object beforeUpdateObject){
            DBUtil.loadHostingPackage(((HostingPackage)beforeUpdateObject).id,new QueryDbListener(){

                        @Override
                        public void queriedSuccessfully(Object result) {
                            mListener.updatedSuccessfully(result);
                        }

                        @Override
                        public void failedToQuery(Throwable throwable) {
                            mListener.failedToUpdate(throwable);
                        }
                    });
        }
        
        private void loadCustomer(Object beforeUpdateObject){
            DBUtil.loadCustomer(((Customer)beforeUpdateObject).id, new QueryDbListener(){

                @Override
                public void queriedSuccessfully(Object result) {
                    mListener.updatedSuccessfully(result);
                }

                @Override
                public void failedToQuery(Throwable throwable) {
                    mListener.failedToUpdate(throwable);
                }
            });
        }
        
        private void loadOrder(Object beforeUpdateObject){
            DBUtil.loadOrder(((Order)beforeUpdateObject).id, new QueryDbListener(){

                @Override
                public void queriedSuccessfully(Object result) {
                    mListener.updatedSuccessfully(result);
                }

                @Override
                public void failedToQuery(Throwable throwable) {
                    mListener.failedToUpdate(throwable);
                }
            });
            
        }
        
    }
    
    private static abstract class DeleteJob extends SQLiteJob {
        
        
        private DeleteDbListener mListener;
        private DeleteJob(final DeleteDbListener listener){
            mListener = listener;
        }
    
  
        @Override
        protected void jobError(Throwable error) throws Throwable {
            super.jobError(error); 
            if (mListener != null)
                mListener.failedToDelete(error);
            
            System.out.println("delete error " + error.getMessage());
        }

        @Override
        protected void jobFinished(Object result) throws Throwable {
            super.jobFinished(result); 
            if (result != null && mListener != null){
               mListener.deletedSuccessfully();
    
    }
        }
    }
    
    
    
    public static interface InsertDbListener {
        void insertedSuccessfully();
        void failedToInsert(Throwable throwable);
    }
    
    public static interface QueryDbListener<RESULT> {
        void queriedSuccessfully(RESULT result);
        void failedToQuery(Throwable throwable);
    }
    
    public static interface UpdateDbListener<RESULT>{
        void updatedSuccessfully(RESULT result);
        void failedToUpdate(Throwable error);
    }
    
    public static interface DeleteDbListener{
        void deletedSuccessfully();
        void failedToDelete(Throwable error);
    }
}
