package Game.Utils;

public class Resource {
    private int currentAmount;
    private int capacity;

    public Resource(int currentAmount, int capacity) {
        this.currentAmount = currentAmount;
        this.capacity = capacity;
    }

    public int getCurrentAmount() {
        return currentAmount;
    }

    public void addCurrentAmount(int amount) {
        this.currentAmount = Math.min(this.currentAmount + amount, this.capacity);
    }

    public void mulCurrentAmount(int add, int mul) {
        this.currentAmount = Math.min((this.currentAmount + add) * mul, this.capacity);
    }

    public void reduceCurrentAmount(int amount) {
        this.currentAmount = Math.max(this.currentAmount - amount, 0);
    }

    public int getCapacity() {
        return capacity;
    }

    public void addCapacity(int capacity) {
        this.capacity += capacity;
    }

    public void restore() {
        this.currentAmount = this.capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return (currentAmount == resource.currentAmount &&
                capacity == resource.capacity);
    }

    @Override
    public String toString() {
        return String.format("%d/%d", currentAmount, capacity);
    }
}
