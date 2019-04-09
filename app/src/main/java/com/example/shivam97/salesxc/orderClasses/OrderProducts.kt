package com.example.shivam97.salesxc.orderClasses

class OrderProducts(var code: String,var name:String,var rate:Float,var quantity:Float){
    var total=0.0f
    init {
        total=rate*quantity
    }


}
