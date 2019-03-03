import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiCallerService {

  private baseUrl = "/portfolio/api";
  private flaskBaseUrl = "/flask";

  constructor(private http: HttpClient) { }

  makeFlaskGetRequest(url: string): Observable<any> {
    url = this.flaskBaseUrl + url;
    return this.http.get(url).pipe(map(res => res));
  }

  makeGetRequest(url: string) : Observable<any> {
    url = this.baseUrl + url;
    return this.http.get(url).pipe(map(res => res));
  }

  makePostRequest(url: string, postBody: any) : Observable<any> {
    url = this.baseUrl + url;
    return this.http.post(url, postBody).pipe(map(res => res));
  }

  makeDeleteRequest(url: string) : Observable<any> {
    url = this.baseUrl + url;
    return this.http.delete(url).pipe(map(res => res));
  }
}
