public interface ItemInterface {
    int getId();
    void setId(int id);
    String getTitle();
    String getDescription();
    double getPrice();
    String getSeller();
    boolean isSold();
    void setSold(boolean sold);
}
