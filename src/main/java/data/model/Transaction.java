package data.model;

public class Transaction {
    private int transactionID;
    private int walletID;
    private String transactionType;
    private double amount;
    private String transactionDate;

    public Transaction() {}

    public Transaction(String transactionType, double amount, String transactionDate) {
        this.transactionType = transactionType;
        this.amount = amount;
        this.transactionDate = transactionDate;
    }

    public int getTransactionID() { return transactionID; }
    public void setTransactionID(int transactionID) { this.transactionID = transactionID; }

    public int getWalletID() { return walletID; }
    public void setWalletID(int walletID) { this.walletID = walletID; }

    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getTransactionDate() { return transactionDate; }
    public void setTransactionDate(String transactionDate) { this.transactionDate = transactionDate; }

    public String getTime(){
        return checkTime(this.transactionDate.substring(11, 16));
    }

    private String checkTime(String time){
        int hour = Integer.parseInt(time.substring(0,2).trim());
        System.out.println(hour);
        if(hour > 12){
            return hour - 12 +  ":" + time.substring(3) + "PM";
        }
        if (hour == 12) {
            return hour + ":" + time.substring(3) + "PM";
        } else {
            return hour +  ":" + time.substring(3) + "AM";
        }
    }
}