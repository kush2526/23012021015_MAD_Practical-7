package com.example.a23012021015_mad_practical_7.db

import org.json.JSONObject
import java.io.Serializable

class Person(var id: String,
             var name: String,
             var emailId: String,
             var phoneNo: String,
             var address: String): Serializable
{
    constructor(jsonObject: JSONObject):this("","","","",""){
        id = jsonObject.getString("id")
        emailId = jsonObject.getString("email")
        phoneNo = jsonObject.getString("phone")
        val profileJson = jsonObject.getJSONObject("profile")
        name = profileJson.getString("name")
        address = profileJson.getString("address")
    }
    override fun toString(): String {
        return "$name\n$phoneNo\n$emailId\n$address"
    }
}