import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ApiCallerService } from '../api-caller.service';

@Component({
  selector: 'app-profile',
  providers: [ApiCallerService],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  private sub: any;
  emailId: string;
  name: string;
  total_investment: number;
  unrealised_difference: number;
  balance: number;
  max_gainer_symbol: string;
  max_gainer_amt: number;
  max_loser_symbol: string;
  max_loser_amt: number;

  constructor(private router: ActivatedRoute, private apiCallerService: ApiCallerService) { }

  ngOnInit() {
    this.sub = this.router.params.subscribe(params => {
      this.emailId = params['email_id'];
    });
    this.apiCallerService.makeGetRequest("/profile/get_user_details/"+this.emailId).subscribe(
      res => {
        if(res.error == null) {
          console.log(res.data);
          if(res.data.type == "individual") {
            this.name = res.data.first_name + " " + res.data.last_name;
          } else if(res.data.type == "company") {
            this.name = res.data.company_name;
          }
          this.total_investment = res.data.total_investment;
          this.unrealised_difference = res.data.unrealised_difference;
          this.balance = res.data.balance;
          this.max_gainer_symbol = res.data.max_gainer_symbol;
          this.max_gainer_amt = res.data.max_gainer_amt;
          this.max_loser_symbol = res.data.max_loser_symbol;
          this.max_loser_amt = res.data.max_loser_amt;
        } else {
          console.log(res.error);
        }
      }
    );
  }

  

}
