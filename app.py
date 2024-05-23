from flask import Flask, jsonify, request
from flask_cors import CORS
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy import Text

app = Flask(__name__)
CORS(app)

app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql://root:@localhost/apitask'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
db = SQLAlchemy(app)

class Tasks(db.Model):
    __tablename__ = 'tasks'
    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    task_name = db.Column(db.String(255), nullable=False)
    task_detail = db.Column(db.String(255), nullable=False)
    urgency = db.Column(db.String(255), nullable=False)
    status = db.Column(db.String(255), nullable=False)
    createdTime = db.Column(db.String(255), nullable=False)
    finishedTime = db.Column(db.String(255), nullable=False)
    duration = db.Column(db.String(255), nullable=False)

    def to_dict(self):
        return {'id': self.id, 'task_name': self.task_name, 'task_detail': self.task_detail, 'urgency': self.urgency,
                'status': self.status, 'createdTime': self.createdTime, 'finishedTime': self.finishedTime,
                'duration': self.duration}

@app.route('/')
def home():
    return jsonify({'message': 'The database is ready to pair'})

@app.route('/tasks', methods=['POST'])
def add_task():
    new_task = request.get_json()
    new_task_data = Tasks(task_name=new_task['task_name'], task_detail=new_task['task_detail'],
                          urgency=new_task['urgency'], status=new_task['status'],
                          createdTime=new_task['createdTime'], finishedTime=new_task['finishedTime'],
                          duration=new_task['duration'])
    db.session.add(new_task_data)
    db.session.commit()
    return jsonify({'message': 'Task added successfully'})

@app.route('/tasks', methods=['GET'])
def get_task():
    status = request.args.get('status')
    urgency = request.args.get('urgency')
    if status and urgency:
        tasks = Tasks.query.filter_by(status=status, urgency=urgency)
    else:
        tasks = Tasks.query.all()
    task_list = [data.to_dict() for data in tasks]
    return jsonify(task_list)

@app.route('/tasks/<int:id>', methods=['DELETE'])
def delete_task(id):
    task_data = Tasks.query.get(id)
    if not task_data:
        return jsonify({'message': 'Task not found'}), 404

    db.session.delete(task_data)
    db.session.commit()
    return jsonify({'message': 'Task data deleted successfully'})

@app.route('/tasks/<int:id>', methods=['GET'])
def get_task_data_by_id(id):
    task_data = Tasks.query.get(id)
    if task_data:
        return jsonify(task_data.to_dict())
    return jsonify({'message': 'Task data not found'}), 404

@app.route('/task_data/<int:id>', methods=['PUT'])
def update_task_data(id):
    task_data = Tasks.query.get(id)
    if not task_data:
        return jsonify({'message': 'Task data not found'}), 404

    data = request.get_json()
    if data["status"] == "done":
        task_data.finishedTime = data['finishedTime']
        task_data.duration = data['duration']
    task_data.status = data['status']
    db.session.commit()
    return jsonify({'message': 'Task data updated successfully'})

if __name__ == '__main__':
    with app.app_context():
        db.create_all()
    app.run(debug=True)
