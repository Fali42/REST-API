# python -m venv api_env
# source api_env/bin/activate

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

#GET
@app.route('/')
def home():
    return jsonify({'message': 'Welcome to the Travel API'})

@app.route('/destinations', methods=['GET'])
def get_destinations():
    destinations = Destination.query.all()
    return jsonify([destination.to_dict() for destination in destinations])

@app.route('/destinations/<int:destination_id>', methods=['GET'])
def get_destination(destination_id):
    destination = Destination.query.get(destination_id)
    if destination:
        return jsonify(destination.to_dict())
    else:
        return jsonify({'message': 'Destination not found'}), 404
    
#POST
@app.route('/destinations', methods=['POST'])
def add_destination():
    data = request.get_json()
    new_destination = Destination(
        destination=data['destination'],
        country=data['country'],
        rating=data['rating']
    )
    
    db.session.add(new_destination)
    db.session.commit()
    return jsonify(new_destination.to_dict()), 201

#PUT (update)
@app.route('/destinations/<int:destination_id>', methods=['PUT'])
def update_destination(destination_id):
    destination = Destination.query.get(destination_id)
    if destination:
        data = request.get_json()
        destination.destination = data['destination']
        destination.country = data['country']
        destination.rating = data['rating']
        
        db.session.commit()
        return jsonify(destination.to_dict())
    else:
        return jsonify({'message': 'Destination not found'}), 404
    

#DELETE
@app.route('/destinations/<int:destination_id>', methods=['DELETE'])
def delete_destination(destination_id):
    destination = Destination.query.get(destination_id)
    if destination:
        db.session.delete(destination)
        db.session.commit()
        return jsonify({'message': 'Destination deleted'})
    else:
        return jsonify({'message': 'Destination not found'}), 404



if __name__ == '__main__':
    app.run(debug=True)
    
    
