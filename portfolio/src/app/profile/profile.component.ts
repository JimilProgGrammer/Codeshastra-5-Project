import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  private sub: any;
  emailId: string;

  constructor(private router: ActivatedRoute) { }

  ngOnInit() {
    this.sub = this.router.params.subscribe(params => {
      this.emailId = params['email_id'];
    });
  }

  

}
