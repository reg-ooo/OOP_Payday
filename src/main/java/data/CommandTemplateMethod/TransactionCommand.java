package data.CommandTemplateMethod;

import data.UserManager;

// TEMPLATE METHOD PATTERN + COMMAND PATTERN
public abstract class TransactionCommand {

    // Template method - defines the transaction workflow (FINAL - cannot be overridden)
    public final boolean execute() {
        if (!validateUser()) {
            System.out.println("User validation failed");
            return false;
        }

        if (!checkBalance()) {
            System.out.println("Insufficient balance or invalid amount");
            return false;
        }

        performTransaction();
        logTransaction();
        refreshUI();

        System.out.println("Transaction completed successfully!");
        return true;
    }

    // Common step - all transactions validate user the same way
    private boolean validateUser() {
        return UserManager.getInstance().revalidateUser();
    }

    // Common step - all transactions refresh UI the same way
    private void refreshUI() {
        UserManager.getInstance().loadComponents();
    }

    // Abstract methods - subclasses MUST implement these (vary by transaction type)
    protected abstract boolean checkBalance();
    protected abstract void performTransaction();
    protected abstract void logTransaction();
}
