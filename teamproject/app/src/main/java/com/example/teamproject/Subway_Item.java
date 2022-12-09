package com.example.teamproject;

public class Subway_Item {
    private String _name;
    private float _density;
    private String _line;

    public String getName() {
        return _name;
    }

    public float getDensity() {
        return _density;
    }

    public String getLine() {
        return _line;
    }

    public Subway_Item(String name,  String line,int density) {
        _name=name;
        _density=density;
        _line=line;
    }

}
