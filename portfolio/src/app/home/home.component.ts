import { Component, OnInit } from '@angular/core';
import { ApiCallerService } from '../api-caller.service';

@Component({
  selector: 'app-home',
  providers: [ApiCallerService],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  emailId: string;
  password: string;
  postbody: any;

  constructor(private apiCallerService: ApiCallerService) { }
 
  ngOnInit() {
  }

  login(){
    this.postbody={
      "email_id": this.emailId,
      "password": this.password
    }
    this.apiCallerService.makePostRequest("/user/login",this.postbody).subscribe(
      res => {
        if(res.error == null) {
          console.log(res.data);
        } else {
          console.log(res.error);
        }
      }
    );
  }
}
