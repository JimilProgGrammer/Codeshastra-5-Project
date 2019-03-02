import { ApiCallerService } from './../api-caller.service';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-company-signup',
  providers: [ApiCallerService],
  templateUrl: './company-signup.component.html',
  styleUrls: ['./company-signup.component.css']
})
export class CompanySignupComponent implements OnInit {

  postBody: any;

  compname: string;
  compcontact: number;
  compemail: string;
  pwd: string;
  confirmPwd: string;
  pan: string;
  accNo: number;
  bankName: string;

  constructor(private apiCallerService: ApiCallerService, private router: Router) { }

  ngOnInit() {
  }

  signUp() {
    this.postBody = {
      "company_name": this.compname,
      "contact": this.compcontact,
      "email_id": this.compemail,
      "password": this.pwd,
      "confirm_password": this.confirmPwd,
      "pan_card": this.pan,
      "acc_no": this.accNo,
      "bank_name": this.bankName
    }
    this.apiCallerService.makePostRequest("/user/company_signup", this.postBody).subscribe(
      res => {
        if(res.error == null) {
          console.log("User Signed Up Successfully");
          this.router.navigate(['/profile', this.compemail]);
        } else {
          console.log(res.error);
        }
      }
    );
  }

}
