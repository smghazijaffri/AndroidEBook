package com.example.item;

import java.io.Serializable;

public class SettingList implements Serializable {

    String Id;
    String Name;
    String Icon;


    public SettingList(String Id,String Name,String Icon){
        this.Id=Id;
        this.Name=Name;
        this.Icon=Icon;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }


}
