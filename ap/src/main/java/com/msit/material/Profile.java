package com.msit.material;


public class Profile {

    public String _ring;
    public String _name;
    public String _media;
    public String _alarm;

    public Profile() {
    }


    public Profile(String name,String ring, String _media, String _alarm) {
        this._ring = ring;
        this._name = name;
        this._media = _media;
        this._alarm = _alarm;

    }

    public String getRing() {
        return this._ring;
    }


    public void setRing(String ring) {
        this._ring = ring;
    }


    public String getName() {
        return this._name;
    }


    public void setName(String name) {
        this._name = name;
    }


    public String getMedia() {
        return this._media;
    }


    public void setMedia(String media) {this._media = media; }


    public String getAlarm() {
        return this._alarm;
    }


    public void setAlarm(String alarm) {
        this._alarm = alarm;
    }
}
