import { ApiCallerService } from './api-caller.service';
import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  providers: [ApiCallerService],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app';

  constructor(private apiCallerService: ApiCallerService) { }

  ngOnInit() {

  }
}