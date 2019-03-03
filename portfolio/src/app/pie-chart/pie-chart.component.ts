import { Component, OnInit, Input } from '@angular/core';
import { ApiCallerService } from '../api-caller.service';
//import { Router } from '@angular/router';

@Component({
  selector: 'app-pie-chart',
  templateUrl: './pie-chart.component.html',
  styleUrls: ['./pie-chart.component.css'],
  providers: [ApiCallerService],
})
export class PieChartComponent implements OnInit {

  @Input() emailId: string;

  symbol: string;
  value: number;
  public pieChartLabels:string[];
  public pieChartData:number[];
  public pieChartType:string = 'pie';

  constructor(private apiCallerService: ApiCallerService) { }

  ngOnInit() {
    this.apiCallerService.makeFlaskGetRequest("/get_chart_data/"+this.emailId).subscribe(
      res => {
        if(res.data != null) {
           console.log(res.data);
           this.pieChartLabels = res.data.symbol;
           this.pieChartData = res.data.value; 
        }
      }
    );
  };


   // Pie
   /*
   public pieChartLabels:string[] = ['Download Sales', 'In-Store Sales', 'Mail Sales'];
   public pieChartData:number[] = [300, 500, 100];
   public pieChartType:string = 'pie';
  */

   // events
   public chartClicked(e:any):void {
     console.log(e);
   }
  
   public chartHovered(e:any):void {
     console.log(e);
   }
   
}
