import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-search-vacuum',
  templateUrl: './search-vacuum.component.html',
})
export class SearchVacuumComponent {
  vacuums: any[] = [];
  url: string = 'http://localhost:8081/vacuums';

  month: number = 2;
  day: number = 9;
  houres: number = 14;
  minutes: number = 0;
  seconds: number = 0;

  formData = {
    name: "",
    status: '',
    dateFrom:  0,
    dateTo: 0,
  };

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.fetchVacuums();
  }

  fetchVacuums() {
    this.http.get<any[]>(this.url)
      .subscribe(
        (data) => {
          this.vacuums = data;
        },
        (error) => {
          window.alert('Error fetching vacuums');
        }
      );
  }

  runVacuum(id: number) {
    this.http.put<any[]>(this.url+'/start/'+ id, null)
      .subscribe(
        (data) => {
          this.vacuums = this.vacuums.filter(vacuum => vacuum.id !== id);
          this.vacuums.push(data);
          this.vacuums.sort((a, b) => a.id - b.id);
        },
        (error) => {
          window.alert('Error running vacuum ' + id);
        }
      );
  }

  stopVacuum(id: number) {
    this.http.put<any[]>(this.url+'/stop/'+ id, null)
      .subscribe(
        (data) => {
          this.vacuums = this.vacuums.filter(vacuum => vacuum.id !== id);
          this.vacuums.push(data);
          this.vacuums.sort((a, b) => a.id - b.id);
        },
        (error) => {
          window.alert('Error stopping vacuum ' + id);
        }
      );
  }

  dischargeVacuum(id: number) {
    this.http.put<any[]>(this.url+'/discharge/'+ id, null)
      .subscribe(
        (data) => {
          this.vacuums = this.vacuums.filter(vacuum => vacuum.id !== id);
          this.vacuums.push(data);
        },
        (error) => {
          window.alert('Error discharging vacuum ' + id);
        }
      );
  }

  //vraca vreme u miliseknudama
  dataScheduler(): number {
    const currentYear = new Date().getFullYear(); // Get the current year
    const currentDate = new Date(currentYear, this.month - 1, this.day, this.houres, this.minutes, this.seconds); // Month is 0-based index in JavaScript Date object

    if(this.month < 1 || this.month > 12 || this.day > 31 || this.day < 1 || 
      this.houres < 0 || this.houres > 23 || this.minutes < 0 || this.minutes > 59 ||
      this.seconds < 0 || this.seconds > 59){
      return -1;
    }
    if (currentDate.getMonth() !== this.month - 1 || currentDate.getDate() !== this.day) {
        return -1;
    }

    this.month = 2;this.day = 6;
    this.houres = 12;this.minutes = 0;this.seconds = 0;

    return currentDate.getTime();
}

  scheduleRun(id: number) {
    let time = this.dataScheduler();
    if(time == -1){
      window.alert('neispravan format datuma');
      return;
    }
    this.http.put<any[]>(this.url+'/schedule/start/'+ id + '?time=' + time, null)
      .subscribe(
        (data) => {
        },
        (error) => {
          window.alert('Error scheduling-running vacuum ' + id);
        }
      );
  }

  scheduleStop(id: number) {
    let time = this.dataScheduler();
    if(time == -1){
      window.alert('neispravan format datuma');
      return;
    }
    this.http.put<any[]>(this.url+'/schedule/stop/'+ id + '?time=' + time, null)
      .subscribe(
        (data) => {
        },
        (error) => {
          window.alert('Error scheduling-stopping vacuum ' + id);
        }
      );
  }

  scheduleDischarge(id: number) {
    let time = this.dataScheduler();
    if(time == -1){
      window.alert('neispravan format datuma');
      return;
    }
    this.http.put<any[]>(this.url+'/schedule/discharge/'+ id + '?time=' + time, null)
      .subscribe(
        (data) => {
        },
        (error) => {
          window.alert('Error scheduling-discharging vacuum ' + id);
        }
      );
  }

  removeVacuum(id: number) {
    this.http.delete<any[]>(this.url+"/"+id)
      .subscribe(
        (data) => {
          this.vacuums = this.vacuums.filter(vacuum => vacuum.id !== id);
        },
        (error) => {
          window.alert('Error removing vacuum ' + id);
        }
      );
  }
  
  searchVacuums(){
    if(this.formData.name == '' && this.formData.status == ''&&
        this.formData.dateFrom == 0 && this.formData.dateTo == 0){
            window.alert('neispravan format za pretragu');
            return;
        }
        let params = "?";
        if(this.formData.name !== ''){
          params += "name="+this.formData.name+"&";
        }
        if(this.formData.status !== ''){
          params += "stat="+this.formData.status+"&";
        }
        if(this.formData.dateFrom !== 0){
          params += "dateFrom="+this.formData.dateFrom+"&";
        }
        if(this.formData.dateTo !== 0){
          params += "dateTo="+this.formData.dateTo;
        }
    this.http.get<any[]>(this.url+"/search"+params)
      .subscribe(
        (data) => {
          this.vacuums = data;
          if(this.formData.status !== ""){
            this.vacuums = data.filter(vacuum => vacuum.status.status === this.formData.status);
          }
          this.formData.name = '';
          this.formData.status = '';
          this.formData.dateFrom = 0;
          this.formData.dateTo = 0;
        },
        (error) => {
          window.alert('Error searching vacuums');
        }
      );
  }
}
