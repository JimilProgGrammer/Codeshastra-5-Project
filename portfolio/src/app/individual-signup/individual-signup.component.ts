import { ApiCallerService } from './../api-caller.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-individual-signup',
  providers: [ApiCallerService],
  templateUrl: './individual-signup.component.html',
  styleUrls: ['./individual-signup.component.css']
})
export class IndividualSignupComponent implements OnInit {

  postBody: any;
  fname: string;
  lname: string;
  contact: number;
  dob: string;
  age: number;
  email: string;
  pwd: string;
  confirmPwd: string;
  pan: string;
  accNo: string;
  bankName: string;

  constructor(private apiCallerService: ApiCallerService) { }

  ngOnInit() {
  }

  signUp() {
    this.postBody = {
      "first_name": this.fname,
      "last_name": this.lname,
      "dob": this.dob,
      "age": this.age,
      "contact": this.contact,
      "email_id": this.email,
      "password": this.pwd,
      "confirm_password": this.confirmPwd,
      "pan_card": this.pan,
      "acc_no": this.accNo,
      "bank_name": this.bankName
    }
    this.apiCallerService.makePostRequest("/user/individual_signup", this.postBody).subscribe(
      res => {
        if(res.error == null) {
          console.log("User Signed Up Successfully");
        } else {
          console.log(res.error);
        }
      }
    );
  }

}
