import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-company-signup',
  templateUrl: './company-signup.component.html',
  styleUrls: ['./company-signup.component.css']
})
export class CompanySignupComponent implements OnInit {

  compname: string;
  compcontact: number;
  compemail: string;
  pwd: string;
  confirmPwd: string;
  pan: string;
  accNo: number;
  bankName: string;

  constructor() { }

  ngOnInit() {
  }

  signUp() {
    console.log(this.compname);
    console.log(this.compcontact);
    console.log(this.compemail);
    console.log(this.pwd);
    console.log(this.confirmPwd);
    console.log(this.pan);
    console.log(this.accNo);
    console.log(this.bankName);
  }

}
