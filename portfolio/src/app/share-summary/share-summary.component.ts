import { Component, OnInit, Input } from '@angular/core';
import { ApiCallerService } from '../api-caller.service';

@Component({
  selector: 'app-share-summary',
  providers: [ApiCallerService],
  templateUrl: './share-summary.component.html',
  styleUrls: ['./share-summary.component.css']
})
export class ShareSummaryComponent implements OnInit {

  @Input() emailId: string;

  portfolioRecords: Array<any>;

  constructor(private apiCallerService: ApiCallerService) { }

  ngOnInit() {
    this.apiCallerService.makeGetRequest("/profile/get_share_summary/"+this.emailId).subscribe(
      res => {
        if(res.error == null) {
          console.log("API Returned");
          this.portfolioRecords = res.data;
          console.log(this.portfolioRecords);
        } else {
          console.log(res.error);
        }
      }
    );
  }

}
