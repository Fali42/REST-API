from flask import Flask, jsonify, request
from flask_sqlalchemy import SQLAlchemy #orm

app = Flask(__name__)

#create database

app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///travel.db'
db = SQLAlchemy(app)

class Destination(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    destination = db.Column(db.String(100), nullable=False)
    country = db.Column(db.String(100), nullable=False)
    rating = db.Column(db.Float, nullable=False)

def to_dict(self):
        return {
            'id': self.id,
            'destination': self.destination,
            'country': self.country,
            'rating': self.rating
        }
        
with app.app_context():
    db.create_all()
    
    
    
#create Routes
#https:// xxxx

@app.route('/')
def home():
    return jsonify({'message': 'Welcome to the Travel API'})

@app.route('/destinations', methods=['GET'])
def get_destinations():
    destinations = Destination.query.all()
    return jsonify([destination.to_dict() for destination in destinations])





if __name__ == '__main__':
    app.run(debug=True) 
    