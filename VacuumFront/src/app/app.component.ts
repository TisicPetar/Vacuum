import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent implements OnInit{
  title = 'nwp-projekat';
  
  constructor(private router: Router) {}

  ngOnInit() {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        sessionStorage.setItem('lastVisitedRoute', event.url);
      }
    });

    const lastVisitedRoute = sessionStorage.getItem('lastVisitedRoute');
    if (lastVisitedRoute) {
      this.router.navigateByUrl(lastVisitedRoute);
    } else {
      this.router.navigateByUrl('/login'); // Redirect to default route if no stored route found
    }
  }
}
