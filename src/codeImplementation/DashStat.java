package codeImplementation;

public class DashStat {

    //Variables
    private String label;//label represents the  movie genre name.
    private int count;//represents how many tickets the user booked for this genre.

    //Constructor
    public DashStat(String label, int count) {
        this.label = label;
        this.count = count;
    }

    //Getters
    public String getLabel(){
        return label;
    }

    public int getCount(){
        return count;
    }
}
