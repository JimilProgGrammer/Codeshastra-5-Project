import { ApiCallerService } from './../api-caller.service';
import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-notifications',
  providers: [ApiCallerService],
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.css']
})
export class NotificationsComponent implements OnInit {

  @Input() emailId: string;
  records: any;
  portfolioRecords: Array<any>;

  constructor(private apiCallerService: ApiCallerService) { }

  ngOnInit() {
    this.apiCallerService.makeGetRequest("/notification/get_all/"+this.emailId).subscribe(
      res => {
        if(res.error == null) {
          this.records = res.data[0];
          this.portfolioRecords = this.records.portfolio;
          console.log(this.records);
          console.log(this.portfolioRecords);
        } else {
          console.log(res.error);
        }
      }
    )
  }

}
