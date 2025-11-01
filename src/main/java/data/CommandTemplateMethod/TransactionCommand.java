package data.CommandTemplateMethod;

import data.UserManager;

// TEMPLATE METHOD PATTERN + COMMAND PATTERN
public abstract class TransactionCommand {

    // Template method - defines the transaction workflow (FINAL - cannot be overridden)
    public final void execute() {
        if (!validateUser()) {
            System.out.println("User validation failed");
            return;
        }

        if (!checkBalance()) {
            System.out.println("Insufficient balance or invalid amount");
            return;
        }

        performTransaction();
        logTransaction();
        refreshUI();

        System.out.println("Transaction completed successfully!");
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
