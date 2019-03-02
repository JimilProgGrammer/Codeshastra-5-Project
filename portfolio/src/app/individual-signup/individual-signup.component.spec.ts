import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IndividualSignupComponent } from './individual-signup.component';

describe('IndividualSignupComponent', () => {
  let component: IndividualSignupComponent;
  let fixture: ComponentFixture<IndividualSignupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IndividualSignupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IndividualSignupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
