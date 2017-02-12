/**
 * Created by Prajal Bishwakarma on 2/11/2017.
 */
package disruptdc.locc;

public class Friend extends Object {

    private String name;
    private Boolean checked;

    public Friend(String name, Boolean checked){
        this.name = name;
        this.checked = checked;
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
}
