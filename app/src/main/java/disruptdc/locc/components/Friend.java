/**
 * Created by Prajal Bishwakarma on 2/11/2017.
 */
package disruptdc.locc.components;

public class Friend extends Object {

    private String name;
    private Boolean checked;
    private double longitude;
    private double latitude;


    public Friend(String name, Boolean checked, double longitude, double latitude){
        this.name = name;
        this.checked = checked;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getChecked(){
        return checked;
    }

    public void setChecked(Boolean checked){
        this.checked = checked;
    }

    public double getLongitude(){
        return this.longitude;
    }

    public double getLatitude(){
        return this.latitude;
    }
}
