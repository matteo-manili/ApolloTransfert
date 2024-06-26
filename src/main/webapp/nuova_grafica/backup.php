<?php include 'header.php';?>
	
	<div class="content-area home-content">
	
		<div class="container">
		
			<div class="main-heading-row">
					<h1 class="text-center">Car Rental With Driver</h1>
					<h3 class="text-center">All Inclusive Price, Transfer Service Throughout Italy.</h3>
			</div>

		
			<div class="col-md-8 col-md-offset-2">
				
				<div class="row info-row info-airport">
					<h2><img src="images/airplane.png">Transfer Aeroporto di Milano-Malpensa (MXP)</h2>
					<h3><img src="images/earth.png">Tariffe Transfer da Aeroporto di Milano-Malpensa (MXP) alle le Citta nelle vicinanze</h3>
					<ul>
						<li>Partenza da Aeroporto di Milano-Malpensa (MXP) arrivo a Varese da 72.12€</li>
						<li>Partenza da Aeroporto di Milano-Malpensa (MXP) arrivo a Varese da 72.12€</li>
					</ul>
					<h3><img src="images/plane.png">Tariffe Transfer da Aeroporto di Milano-Malpensa (MXP) alle le Citta nelle vicinanze</h3>
					<ul>
						<li>Partenza da Aeroporto di Milano-Malpensa (MXP) arrivo a Varese da 72.12€</li>
						<li>Partenza da Aeroporto di Milano-Malpensa (MXP) arrivo a Varese da 72.12€</li>
					</ul>
				</div>
				
				<div class="row info-row info-attraction">
					<h2><img src="images/bank.png">Transfer Aeroporto di Milano-Malpensa (MXP)</h2>
					<h3><img src="images/earth.png">Tariffe Transfer da Aeroporto di Milano-Malpensa (MXP) alle le Citta nelle vicinanze</h3>
					<ul>
						<li>Partenza da Aeroporto di Milano-Malpensa (MXP) arrivo a Varese da 72.12€</li>
						<li>Partenza da Aeroporto di Milano-Malpensa (MXP) arrivo a Varese da 72.12€</li>
					</ul>
					<h3><img src="images/plane.png">Tariffe Transfer da Aeroporto di Milano-Malpensa (MXP) alle le Citta nelle vicinanze</h3>
					<ul>
						<li>Partenza da Aeroporto di Milano-Malpensa (MXP) arrivo a Varese da 72.12€</li>
						<li>Partenza da Aeroporto di Milano-Malpensa (MXP) arrivo a Varese da 72.12€</li>
					</ul>
				</div>
				
				<div class="row info-row info-city">
					<h2><img src="images/earth-globe.png">Transfer Aeroporto di Milano-Malpensa (MXP)</h2>
					<h3><img src="images/earth.png">Tariffe Transfer da Aeroporto di Milano-Malpensa (MXP) alle le Citta nelle vicinanze</h3>
					<ul>
						<li>Partenza da Aeroporto di Milano-Malpensa (MXP) arrivo a Varese da 72.12€</li>
						<li>Partenza da Aeroporto di Milano-Malpensa (MXP) arrivo a Varese da 72.12€</li>
					</ul>
					<h3><img src="images/plane.png">Tariffe Transfer da Aeroporto di Milano-Malpensa (MXP) alle le Citta nelle vicinanze</h3>
					<ul>
						<li>Partenza da Aeroporto di Milano-Malpensa (MXP) arrivo a Varese da 72.12€</li>
						<li>Partenza da Aeroporto di Milano-Malpensa (MXP) arrivo a Varese da 72.12€</li>
					</ul>
				</div>
				
				<div class="row info-row info-port">
					<h2><img src="images/cruise.png">Transfer Aeroporto di Milano-Malpensa (MXP)</h2>
					<h3><img src="images/earth.png">Tariffe Transfer da Aeroporto di Milano-Malpensa (MXP) alle le Citta nelle vicinanze</h3>
					<ul>
						<li>Partenza da Aeroporto di Milano-Malpensa (MXP) arrivo a Varese da 72.12€</li>
						<li>Partenza da Aeroporto di Milano-Malpensa (MXP) arrivo a Varese da 72.12€</li>
					</ul>
					<h3><img src="images/plane.png">Tariffe Transfer da Aeroporto di Milano-Malpensa (MXP) alle le Citta nelle vicinanze</h3>
					<ul>
						<li>Partenza da Aeroporto di Milano-Malpensa (MXP) arrivo a Varese da 72.12€</li>
						<li>Partenza da Aeroporto di Milano-Malpensa (MXP) arrivo a Varese da 72.12€</li>
					</ul>
				</div>
				
				
				<div class="row steps-row">
					<ul>
						<li class="step-1 first step-active"><span><img src="images/step-search.png">Search Transfers</span></li>
						<li class="step-2 second"><span><img src="images/step-available.png">Available Cars</span></li>
						<li class="step-3 third"><span><img src="images/step-payment.png">Payment</span></li>
						<li class="step-4 last"><span><img src="images/step-summary.png">Summary</span></li>
					</ul>
					
					
				</div>
				<div class="row form-row">
					<form data-toggle="validator" role="form">
					
						<div class="form-step-1">

						  <div class="form-group input-group">
								<span class="input-group-addon"><i class="fas fa-map-marker-alt"></i></span>
							  <input class="form-control" data-error="Please enter Departure." id="inputDeparture" placeholder="Departure"  type="text" required />
							  <div class="help-block with-errors"></div>
						  </div>
						  
						  <div class="form-group input-group">
								<span class="input-group-addon"><i class="fas fa-map-marker-alt"></i></span>
							  <input class="form-control" data-error="Please enter Arrival." id="inputArrival" placeholder="Arrival"  type="text" required />
							  <div class="help-block with-errors"></div>
						  </div>
						  
						  <div class="form-group input-group invert-form-group">
							  <!--<Button class="form-control" id="invert-da" >Invert Departure Arrival</button> -->
							  <input type="button" class="form-control" id="invert-da"  value="Invert Departure Arrival" onclick="invert()">
						  </div>

						  <div class="form-group input-group date-control">
								<span class="input-group-addon"><i class="far fa-calendar-alt"></i></span>
							  <input class="form-control datepicker-departure datepicker" data-error="Please enter Date." id="datepicker-departure" placeholder="Date"  type="text" required />
							  <div class="help-block with-errors"></div>
						  </div>
						  
						  <div class="form-group input-group time-control">
								<span class="input-group-addon"><i class="far fa-clock"></i></span>
								<input class="form-control" data-error="Please enter Time." id="timepicker-departure" placeholder="10:00 AM"  type="text" required />
								<span class="radio-holder">
									<label class="custom-check">  <span class="ret-txt">Return</span>
									  <input type="checkbox" id="ret-check" name="ret-check">
									  <span class="checkmark"></span>
									</label>
								</span>
							  <div class="help-block with-errors"></div>
						  </div>
						  
						  <div class="return-date-time">
							  <div class="form-group input-group date-control">
									<span class="input-group-addon"><i class="far fa-calendar-alt"></i></span>
								  <input class="form-control datepicker-arrival datepicker" data-error="Please enter Date." id="datepicker-arrival" placeholder="Date"  type="text" />
								  <div class="help-block with-errors"></div>
							  </div>
							  
							  <div class="form-group input-group time-control">
									<span class="input-group-addon"><i class="far fa-clock"></i></span>
									<input class="form-control" data-error="Please enter Time." id="timepicker-arrival" placeholder="10:00 AM"  type="text" />
								  <div class="help-block with-errors"></div>
							  </div>
						  </div>
						  
						  <div class="form-group input-group">
								<span class="input-group-addon"><i class="fas fa-user"></i></span>
							  <input class="form-control" data-error="Please enter Person Details." id="inputPerson" placeholder="Person"  type="text" required />
							  <div class="help-block with-errors"></div>
						  </div>
						  
						  <div class="form-group input-group form-ImpInfo">
								<span class="input-group-addon"><i class="far fa-file-alt"></i></span>
							  <textarea  class="form-control" placeholder="Important Information" rows="6" data-error="Please enter Person Details." id="inputImpInfo"></textarea>
							  <div class="help-block with-errors"></div>
						  </div>

						  <div class="form-group">
							  <button class="btn btn-primary" id="submit-step-1">
								  PROCEED
							  </button>
						  </div>
						  
						</div>
						
						
						<div class="form-step-2">

						  <p style="color:#fff;">Second form fields</p>

						  <div class="form-group">
							  <button class="btn btn-primary" id="submit-step-2">
								  PROCEED
							  </button>
						  </div>
						  
						</div>
						
						<div class="form-step-3">

						  <p style="color:#fff;">Third form fields</p>

						  <div class="form-group">
							  <button class="btn btn-primary" id="submit-step-3">
								  PROCEED
							  </button>
						  </div>
						  
						</div>
						
						<div class="form-step-4">

						  <p style="color:#fff;">Fourth form fields</p>

						  <div class="form-group">
							  <button class="btn btn-primary" id="submit-step-4">
								  PROCEED
							  </button>
						  </div>
						  
						</div>
						
						
					</form>
				</div>
				
			</div>
			
		</div>
	
	</div>
	
<?php include 'footer.php';?>