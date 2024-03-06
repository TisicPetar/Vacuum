import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-error-history',
  templateUrl: './error-history.component.html',
})
export class ErrorHistoryComponent implements OnInit {
  errors: any[] = [];
  currentPage = 1;
  pageSize = 3;

  constructor(private http: HttpClient, private router:Router) { }

  ngOnInit(): void {
    this.fetchErrorHistory();
    this.currentPage = 0;
  }

  fetchErrorHistory() {
    this.http.get<any[]>('http://localhost:8081/errorHistory')
      .subscribe(
        (data) => {
          this.errors = data;
        },
        (error) => {
          window.alert('Error fetching error history:');
        }
      );
  }

  fetchNextPage(){
    this.currentPage = this.currentPage + 1;
    const url = 'http://localhost:8081/errorHistory?page='+this.currentPage+'&size='+this.pageSize;
    this.http.get<any[]>(url)
      .subscribe(
        (data) => {
          if(data.length == 0){
            this.currentPage = this.currentPage - 1;
            return;
          }
          this.errors = data;
        },
        (error) => {
          window.alert('Error fetching error history:');
        }
    )
  }

  fetchPreviousPage(){
    if(this.currentPage - 1 < 0){
      return;
    }
    this.currentPage = this.currentPage - 1;
    const url = 'http://localhost:8081/errorHistory?page='+this.currentPage+'&size='+this.pageSize;
    this.http.get<any[]>(url)
      .subscribe(
        (data) => {
          if(data.length == 0){
            this.currentPage = this.currentPage + 1;
            return;
          }
          this.errors = data;
        },
        (error) => {
          window.alert('Error fetching error history:');
        }
    )
  }

  transform(value: number): string {
    if (!value) {
      return '';
    }
    const date = new Date(value);
    // Format the date as needed (e.g., 'MMM dd, yyyy HH:mm:ss')
    const formattedDate = date.toLocaleString(); // Example format, adjust as per your requirements
    return formattedDate;
  }
}
