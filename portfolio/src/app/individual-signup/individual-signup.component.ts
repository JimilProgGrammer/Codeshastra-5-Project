import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-individual-signup',
  templateUrl: './individual-signup.component.html',
  styleUrls: ['./individual-signup.component.css']
})
export class IndividualSignupComponent implements OnInit {

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

  constructor() { }

  ngOnInit() {
  }

  signUp() {
    console.log(this.fname);
    console.log(this.lname);
    console.log(this.contact);
    console.log(this.dob);
    console.log(this.age);
    console.log(this.email);
    console.log(this.pan);
    console.log(this.accNo);
    console.log(this.bankName);
  }

}
