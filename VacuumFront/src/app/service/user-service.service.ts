import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserServiceService {
  [x: string]: any;

  private apiUrl = 'http://localhost:8081';

  constructor(private http: HttpClient) {

  }

  login(username:string, password:string){

    const body = {
      username: username,
      password: password
    };

    return this.http.post<any>(`${this.apiUrl}/auth/login`,body);

  }

  getAllUsers(){
    return this.http.get<any>(`${this.apiUrl}/users`);
  }

  deleteUser(id:number){

    const options = {
      headers: {
        'Content-Type': 'application/json',
      },
      body: {id}
    };

    return this.http.delete<any>(`${this.apiUrl}/users`, options);
  }

  addUser(username:string, password:string, permisions: any){
    const body = {
      username: username,
      password: password,
      permissions:permisions
    };

    return this.http.post<any>(`${this.apiUrl}/users`,body);
  }

  editUser(username:string, permisions: any){
    const body = {
      id: localStorage.getItem("editer"),
      username: username,
      permissions:permisions
    };

    return this.http.put<any>(`${this.apiUrl}/users`,body);
  }
}
