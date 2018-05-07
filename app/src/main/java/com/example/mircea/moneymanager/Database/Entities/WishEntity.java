package com.example.mircea.moneymanager.Database.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class WishEntity {

    @PrimaryKey(autoGenerate = true)
    public int wishId;

    @ColumnInfo(name = "WishName")
    public String wishName;

    @ColumnInfo(name = "WishDesc")
    public String wishDesc;

    public WishEntity(String wishName, String wishDesc){
        this.wishName = wishName;
        this.wishDesc = wishDesc;
    }

}
