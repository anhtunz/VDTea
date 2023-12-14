package com.example.vdtea;

public class ModelClass {
    private String name;
    private boolean isSelected;

    public ModelClass(String name, boolean isSelected) {
        this.name = name;
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString(){
        return "ModelClass{ " +
                "name = " + name + '\'' +
                ", is selected = "+isSelected + "}";
    }
}
