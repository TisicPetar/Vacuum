import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-add-vacuum',
  templateUrl: './add-vacuum.component.html',
})
export class AddVacuumComponent {
  vacuumName: string = '';

  constructor(private http: HttpClient) { }

  submitForm() {
    const vacuumData = {
      name: this.vacuumName
    };

    this.http.post<any>('http://localhost:8081/vacuums', vacuumData)
      .subscribe(
        (data) => {
          this.vacuumName = '';
        },
        (error) => {
          window.alert("NO PERMISSION");
        }
      );
  }
}
