import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiCallerService {

  private baseUrl = "/portfolio/api";

  constructor(private http: HttpClient) { }

  makeGetRequest(url: string) : Observable<any> {
    url = this.baseUrl + url;
    return this.http.get(url).pipe(map(res => res));
  }

  makePostRequest(url: string) : Observable<any> {
    url = this.baseUrl + url;
    var postBody = {};
    return this.http.post(url, postBody).pipe(map(res => res));
  }

  makeDeleteRequest(url: string) : Observable<any> {
    url = this.baseUrl + url;
    return this.http.delete(url).pipe(map(res => res));
  }
}
