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
  public pieChartLabels:string[] = ['NESTLEIND','TTKPRESTIG','TCS','SKFINDIA'];
  public pieChartData:number[] = [30, 20, 10, 40];
  public pieChartType:string = 'pie';

  constructor(private apiCallerService: ApiCallerService) { }

  ngOnInit() {
    // console.log("Calling API");
    // console.log("At Pie Chart: Email ID = " + this.emailId);
    // this.apiCallerService.makeGetRequest("/chart/get_chart_data/"+this.emailId).subscribe(
    //   res => {
    //     console.log("At PIE CHART COMPONENT --> " + res);
    //     if(res.data != null) {
    //        console.log(res.data);
    //        this.pieChartLabels = res.data.symbol;
    //        this.pieChartData = res.data.value;
    //     }
    //   }
    // );
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
