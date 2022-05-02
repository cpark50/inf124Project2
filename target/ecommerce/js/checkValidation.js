function CheckValidation(frm) {
    var cr = frm.country;
    var fname = frm.fname;
    var lname = frm.lname;
    var phone = frm.phone;
    var address1 = frm.address1;
    var city = frm.city;
    var state = frm.state;
    var zip = frm.zip;
    var payment = frm.payment;
    var payNum = frm.card;
    var fullName = frm.fullname;
    var expDate = frm.expDate;
    
    if(cr.value==""){
        alert("Missing country/region!")
        return (false);
    }
    if(fname.value=="" || lname.value==""){
        alert("Missing name!");
        return (false);
    }
    
    var phone_regex = /^\([1-9][0-9]{2}\)\-[0-9]{3}-[0-9]{4}$/;
    if(!(phone_regex.test(phone.value))){
        alert("Invalid phone number!");
        return (false);
    }
    
    if(address1.value==""){
        alert("Missing address!");
        return (false);
    }
    if(city.value==""){
        alert("Missing city!");
        return (false);
    }
    if(state.value==""){
        alert("Missing state!");
        return (false);
    }
    var zip_regex = /^[1-9][0-9]{4}$/;
    if(!(zip_regex.test(zip.value))){
        alert("Invalid zip code!");
        return (false);
    }        
    if(payment.value=="None"){
        alert("Missing payment method!");
        return (false);
    }
    if(payNum.value=="" || isNaN(payNum.value) || parseInt(payNum.value)<=0){
        alert("Invalid card number!");
        return (false);
    }
    if(fullName.value==""){
        alert("Missing name on card!");
        return (false);
    }
    
    if(expDate.value=="mm/yyyy"){
        alert("Missing expiration date!");
        return (false);
    }
    var date_regex = /^(0[1-9]|1[0-2])\/(202[2-9])$/;
    if(!(date_regex.test(expDate.value))){
        alert("Invalid expiration date!");
        return (false);
    }
    return (true);
}